package com.courseassignment.common;

import lombok.Data;
import java.util.List;

/**
 * 分页响应结果类
 */
@Data
public class PageResult<T> {

    private Long total;
    private Integer page;
    private Integer size;
    private List<T> records;

    public static <T> PageResult<T> of(Long total, Integer page, Integer size, List<T> records) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(total);
        result.setPage(page);
        result.setSize(size);
        result.setRecords(records);
        return result;
    }
}
