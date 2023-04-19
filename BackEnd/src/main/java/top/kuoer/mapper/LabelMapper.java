package top.kuoer.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.kuoer.entity.Label;

import java.util.List;

@Mapper
@Repository
public interface LabelMapper {

    @Select("select * from label")
    List<Label> findLabelAll();

    @Select("select * from label where id=#{id}")
    Label findLabelById(@Param("id") int id);

    @Select("select id from label where name=#{name}")
    Integer findLabelByName(@Param("name") String name);

    @Insert(value="insert into label (name) values (#{name})")
    @SelectKey(keyProperty = "id", keyColumn = "id", before = false, resultType = Integer.class, statement = "select last_insert_rowid()")
    boolean addLabel(Label label);

}
