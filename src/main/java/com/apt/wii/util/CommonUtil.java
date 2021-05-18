package com.apt.wii.util;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class CommonUtil {

    public static <T> ResponseEntity<List<T>> getPaginatedResponseEntity(Page<T> input) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Pages", input.getTotalPages() + "");
        headers.add("X-Total-Elements", input.getTotalElements() + "");
        headers.add("X-Size", input.getSize() + "");
        headers.add("X-Number", input.getNumber() + "");
        headers.add("X-Number-Of-Elements", input.getNumberOfElements() + "");
        headers.add("X-Is-First", input.isFirst() + "");
        headers.add("X-Is-Last", input.isLast() + "");
        headers.add("X-Is-Empty", input.isEmpty() + "");
        return ResponseEntity.ok().headers(headers).body(input.getContent());
    }
}
