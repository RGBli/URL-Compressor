package com.lbw.compressurl.domain;

import lombok.Data;

@Data
public class Receiver {
    private String url;
    private long expire;
}
