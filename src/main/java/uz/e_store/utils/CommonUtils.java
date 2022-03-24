package uz.e_store.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CommonUtils {
    public static void validatePageAndSize(int page, int size) {
        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("Page soni " + AppConstants.MAX_PAGE_SIZE + "dan oshishi mumkin emas !");
        }
        if (page < 0) {
            throw new IllegalArgumentException("Page soni manfiy bolishi mumkin emas !");
        }
    }

    public static Pageable getPageable(int page, int size) {
        validatePageAndSize(page, size);
        return PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    }
    public static Pageable getPageable(int page, int size,String order) {
        validatePageAndSize(page, size);
        return PageRequest.of(page, size, Sort.Direction.DESC, order!=null?order:"createdAt");
    }

    public static Pageable getPageable(int page, int size,String order,Sort.Direction direction) {
        validatePageAndSize(page, size);
        return PageRequest.of(page, size, direction, order!=null?order:"createdAt");
    }

}
