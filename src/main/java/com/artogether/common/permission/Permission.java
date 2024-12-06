package com.artogether.common.permission;

import com.artogether.common.perm_desc.PermDesc;
import com.artogether.common.system_mamager.SystemManager;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "permission")
@IdClass(Permission.PermissionId.class)
public class Permission {

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "manager_id", referencedColumnName = "id")
	private SystemManager manager;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "desc_id", referencedColumnName = "id")
	private PermDesc permDesc;

	// 無參數建構子
	public Permission() {
		super();
	}

	// 有參數
	public Permission(SystemManager manager, PermDesc permDesc) {
		this.manager = manager;
		this.permDesc = permDesc;
	}

	// Getter and Setter
	public SystemManager getManager() {
		return manager;
	}

	public void setManager(SystemManager manager) {
		this.manager = manager;
	}

	public PermDesc getPermDesc() {
		return permDesc;
	}

	public void setPermDesc(PermDesc permDesc) {
		this.permDesc = permDesc;
	}

	//	======================
	// 以下複合主鍵setter getter
	// 	======================

	// Composite Key for Permission
	public void setPermissionId(PermissionId permissionId) {
		this.manager.setId(permissionId.getManagerId());
		this.permDesc.setId(permissionId.getDescId());
	}

	public PermissionId getPermissionId() {
		return new PermissionId(this.manager.getId(), this.permDesc.getId());
	}

	public static class PermissionId implements Serializable {

		private Integer managerId;
		private Integer descId;

		public PermissionId() {
			super();
		}

		public PermissionId(Integer managerId, Integer descId) {
			this.managerId = managerId;
			this.descId = descId;
		}

		public Integer getManagerId() {
			return managerId;
		}

		public void setManagerId(Integer managerId) {
			this.managerId = managerId;
		}

		public Integer getDescId() {
			return descId;
		}

		public void setDescId(Integer descId) {
			this.descId = descId;
		}

		@Override
		public int hashCode() {
			return Objects.hash(managerId, descId);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null || getClass() != obj.getClass()) return false;
			PermissionId that = (PermissionId) obj;
			return Objects.equals(managerId, that.managerId) && Objects.equals(descId, that.descId);
		}
	}
}
