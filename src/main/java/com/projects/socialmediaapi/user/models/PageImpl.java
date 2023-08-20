package com.projects.socialmediaapi.user.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class PageImpl<T> implements Page<T> {
    private final List<T> content;
    private final Pageable pageable;
    private final int total;
    @Override
    public int getTotalPages() {
        return content.isEmpty() ? 0 : (int) Math.ceil((double) total / (double) pageable.getPageSize());
    }

    @Override
    public long getTotalElements() {
        return total;
    }

    @Override
    public int getNumber() {
        return pageable.getPageNumber();
    }

    @Override
    public int getSize() {
        return pageable.getPageSize();
    }

    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public Sort getSort() {
        return pageable.getSort();
    }

    @Override
    public boolean isFirst() {
        return !hasPrevious();
    }

    @Override
    public boolean isLast() {
        return !hasNext();
    }

    @Override
    public boolean hasNext() {
        return getNumber() < getTotalPages() - 1;
    }

    @Override
    public boolean hasPrevious() {
        return getNumber() > 0;
    }

    @Override
    public Pageable nextPageable() {
        return pageable.isPaged() && !isLast()
                ? pageable.next()
                : Pageable.unpaged();
    }

    @Override
    public Pageable previousPageable() {
        return pageable.isPaged() && !isFirst()
                ? pageable.previousOrFirst()
                : Pageable.unpaged();
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        List<U> convertedContent = content.stream().map(converter).collect(Collectors.toList());
        return new PageImpl<>(convertedContent, pageable, total);
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }
}
