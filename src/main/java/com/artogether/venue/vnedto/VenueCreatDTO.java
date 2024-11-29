package com.artogether.venue.vnedto;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.venue.venue.VenueStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueCreatDTO {
    private Integer vneId;
    @NotEmpty(message="場地名稱: 請勿空白")
    @Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$", message = "場地名稱: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間")
    private String name;
//    private BusinessMember businessMember;

    //場地類別描述
    private String type;
    //場地描述
    private String description;

    //可容許預約天數
//    @Min(1)
//    @Max(20)
    //下拉選單
    @NotEmpty
    private Integer availableDays;
    @NotNull(message = "圖片文件不能為空")
    private byte[] imageFile1;
    private byte[] imageFile2;
    private byte[] imageFile3;

    // 自定義校驗邏輯
    public boolean isImageValid() {
        return imageFile1 != null && imageFile1.length > 0;
    }
}
