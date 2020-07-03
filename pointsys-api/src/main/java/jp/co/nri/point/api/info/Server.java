package jp.co.nri.point.api.info;

import java.util.List;

public class Server {

    private Cpu cpu;

    private Mem mem;

    private Jvm jvm;

    private Sys sys;
    
    private List<SysFile> sysFiles;

    public Cpu getCpu() {
        return cpu;
    }

    public void setCpu(Cpu cpu) {
        this.cpu = cpu;
    }

    public Mem getMem() {
        return mem;
    }

    public void setMem(Mem mem) {
        this.mem = mem;
    }

    public Jvm getJvm() {
        return jvm;
    }

    public void setJvm(Jvm jvm) {
        this.jvm = jvm;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public List<SysFile> getSysFiles() {
        return sysFiles;
    }

    public void setSysFiles(List<SysFile> sysFiles) {
        this.sysFiles = sysFiles;
    }

    @Override
    public String toString() {
        return "Server [cpu=" + cpu + ", mem=" + mem + ", jvm=" + jvm + ", sys=" + sys + ", sysFiles=" + sysFiles + "]";
    }

}
