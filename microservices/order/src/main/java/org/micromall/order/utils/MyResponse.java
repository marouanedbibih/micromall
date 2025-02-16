package org.micromall.order.utils;

import lombok.Builder;

@Builder
public record MyResponse(
        String message,
        Object data,
        Object meta) {

}
