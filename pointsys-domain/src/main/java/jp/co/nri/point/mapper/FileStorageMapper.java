package jp.co.nri.point.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.co.nri.point.domain.FileStorage;

@Mapper
public interface FileStorageMapper extends BaseMapper<FileStorage> {

    void deleteByUuid(@Param("uuid") String uuid);

}
