package kz.test.lmssystem.mapper;

import kz.test.lmssystem.dto.ChapterDto;
import kz.test.lmssystem.entity.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChapterMapper {

    @Mapping(target = "chapterName", source = "name")
    ChapterDto toDtoChapter(Chapter chapter);

    @Mapping(target = "name", source = "chapterName")
    Chapter toEntityChapter(ChapterDto chapterDto);

    List<ChapterDto> toDtoChapterList(List<Chapter> chapters);

    List<Chapter> toEntityChapterList(List<ChapterDto> dtoListChapterDto);
}
