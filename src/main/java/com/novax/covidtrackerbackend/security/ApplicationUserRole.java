package com.novax.covidtrackerbackend.security;


import java.util.Set;
import java.util.stream.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Sets;
public enum ApplicationUserRole {
    ADMIN(Sets.newHashSet(
            ApplicationUserPermission.MOH_ADMIN_WRITE,
            ApplicationUserPermission.MOH_ADMIN_READ)),

    MOH_ADMIN(Sets.newHashSet(
            ApplicationUserPermission.MOH_ADMIN_WRITE,
            ApplicationUserPermission.MOH_ADMIN_READ,
            ApplicationUserPermission.MOH_USER_READ,
            ApplicationUserPermission.MOH_USER_WRITE)),

    MOH_USER(Sets.newHashSet(
            ApplicationUserPermission.HOSPITAL_ADMIN_READ,
            ApplicationUserPermission.HOSPITAL_ADMIN_WRITE)),

    HOSPITAL_ADMIN(Sets.newHashSet(
            ApplicationUserPermission.HOSPITAL_USER_READ,
            ApplicationUserPermission.HOSPITAL_USER_WRITE,
            ApplicationUserPermission.PATIENT_READ,
            ApplicationUserPermission.PATIENT_WRITE)),

    PATIENT(Sets.newHashSet(
            ApplicationUserPermission.PATIENT_WRITE)),

    HOSPITAL_USER(Sets.newHashSet(
            ApplicationUserPermission.PATIENT_READ,
            ApplicationUserPermission.PATIENT_WRITE));


    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
		this.permissions = permissions;
	}

	public Set<ApplicationUserPermission> getPermissions() {
		return permissions;
	}

    public Set<SimpleGrantedAuthority> getGrantedAuthority() {
        Set<SimpleGrantedAuthority> permissions =
        getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return   permissions;
    }


}
