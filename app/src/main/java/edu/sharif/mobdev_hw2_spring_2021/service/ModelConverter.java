package edu.sharif.mobdev_hw2_spring_2021.service;


import edu.sharif.mobdev_hw2_spring_2021.db.entity.Bookmark;
import edu.sharif.mobdev_hw2_spring_2021.model.coin.BookmarkDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModelConverter {
    private static final ModelConverter MODEL_CONVERTER = new ModelConverter();

    public static ModelConverter getInstance() {
        return MODEL_CONVERTER;
    }

    public Bookmark getBookmarkEntity(BookmarkDTO bookmarkDTO) {
        Bookmark bookmark = new Bookmark();
        bookmark.setName(bookmarkDTO.getName());
        bookmark.setLongitude(Double.valueOf(bookmarkDTO.getLongitude()));
        bookmark.setLatitude(Double.valueOf(bookmarkDTO.getLatitude()));
        return bookmark;
    }

    public BookmarkDTO getBookmarkDTO(Bookmark bookmark) {
        BookmarkDTO bookmarkDTO = new BookmarkDTO();
        bookmarkDTO.setDbId(bookmark.getDbId());
        bookmarkDTO.setName(bookmark.getName());
        bookmarkDTO.setLongitude(String.valueOf(bookmark.getLongitude()));
        bookmarkDTO.setLatitude(String.valueOf(bookmark.getLatitude()));
        return bookmarkDTO;
    }
}
