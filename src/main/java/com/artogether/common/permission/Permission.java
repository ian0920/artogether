package com.artogether.common.permission;

import com.artogether.common.perm_desc.PermDesc;
import com.artogether.common.system_mamager.SystemManager;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(Permission.PermissionId.class)
public class Permission {
	// 平台功能权限

	@Id
	@Column(name = "manager_id")
	private Integer managerId;  // 作为复合主键的一部分

	@Id
	@Column(name = "desc_id")
	private Integer descId;  // 作为复合主键的一部分

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "manager_id", referencedColumnName = "id", insertable = false, updatable = false)
	private SystemManager manager; // 不允许插入或更新该列

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "desc_id", referencedColumnName = "id", insertable = false, updatable = false)
	private PermDesc permDesc; // 不允许插入或更新该列

	/**
	 * ====================================================================
	 */
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PermissionId implements Serializable {
		private Integer managerId;  // 复合主键的第一部分
		private Integer descId;     // 复合主键的第二部分
	}
}
