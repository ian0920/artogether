package com.artogether.common.permission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor /*建構子*/
@NoArgsConstructor
@Builder
public class PermissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer managerId;
    private Integer descId;
    private String descName;

}
