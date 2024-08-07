package com.scg.stop.aihub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AiHubModelRequest {

    private List<String> task;

    private List<String> dataType;

    private List<String> framework;

}
