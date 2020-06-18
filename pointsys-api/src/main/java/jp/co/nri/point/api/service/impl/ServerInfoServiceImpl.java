package jp.co.nri.point.api.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import jp.co.nri.point.api.info.Cpu;
import jp.co.nri.point.api.info.Jvm;
import jp.co.nri.point.api.info.Mem;
import jp.co.nri.point.api.info.Server;
import jp.co.nri.point.api.info.Sys;
import jp.co.nri.point.api.info.SysFile;
import jp.co.nri.point.api.service.ServerInfoService;
import jp.co.nri.point.util.Arith;
import jp.co.nri.point.util.IPUtils;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.CentralProcessor.TickType;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

@Service
public class ServerInfoServiceImpl implements ServerInfoService {

    private static final int OSHI_WAIT_SECOND = 1000;

    @Override
    public Server getServerInfo() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        Server server = new Server();
        server.setCpu(setCpuInfo(hal.getProcessor()));
        server.setMem(setMemInfo(hal.getMemory()));
        server.setJvm(setJvmInfo());
        server.setSys(setSysInfo());
        server.setSysFiles(setSysFiles(si.getOperatingSystem()));
        return server;
    }

    private Cpu setCpuInfo(CentralProcessor processor) {
        Cpu cpu = new Cpu();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
        long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        cpu.setCpuNum(processor.getLogicalProcessorCount());
        cpu.setTotal(totalCpu);
        cpu.setSys(cSys);
        cpu.setUsed(user);
        cpu.setWait(iowait);
        cpu.setFree(idle);
        return cpu;
    }

    /**
     * 设置内存信息
     */
    private Mem setMemInfo(GlobalMemory memory) {
        Mem mem = new Mem();
        mem.setTotal(memory.getTotal());
        mem.setUsed(memory.getTotal() - memory.getAvailable());
        mem.setFree(memory.getAvailable());
        return mem;
    }

    /**
     * 设置服务器信息
     */
    private Sys setSysInfo() {
        Sys sys = new Sys();
        Properties props = System.getProperties();
        sys.setComputerName(IPUtils.getHostName());
        sys.setComputerIp(IPUtils.getHostIp());
        sys.setOsName(props.getProperty("os.name"));
        sys.setOsArch(props.getProperty("os.arch"));
        sys.setUserDir(props.getProperty("user.dir"));
        return sys;
    }

    /**
     * 设置Java虚拟机
     */
    private Jvm setJvmInfo() {
        Jvm jvm = new Jvm();
        Properties props = System.getProperties();
        jvm.setTotal(Runtime.getRuntime().totalMemory());
        jvm.setMax(Runtime.getRuntime().maxMemory());
        jvm.setFree(Runtime.getRuntime().freeMemory());
        jvm.setVersion(props.getProperty("java.version"));
        jvm.setHome(props.getProperty("java.home"));
        return jvm;
    }

    /**
     * 设置磁盘信息
     */
    private List<SysFile> setSysFiles(OperatingSystem os) {
        List<SysFile> sysFiles = new LinkedList<>();
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            SysFile sysFile = new SysFile();
            sysFile.setDirName(fs.getMount());
            sysFile.setSysTypeName(fs.getType());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(convertFileSize(total));
            sysFile.setFree(convertFileSize(free));
            sysFile.setUsed(convertFileSize(used));
            sysFile.setUsage(Arith.mul(Arith.div(used, total, 4), 100));
            sysFiles.add(sysFile);
        }
        return sysFiles;
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    private String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

}
