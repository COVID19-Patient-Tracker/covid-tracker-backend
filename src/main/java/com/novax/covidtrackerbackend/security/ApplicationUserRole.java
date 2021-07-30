package com.novax.covidtrackerbackend.security;






import java.util.Set;
import java.util.stream.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Sets;

public enum ApplicationUserRole {
    ADMIN(Sets.newHashSet(
            ApplicatioUserPermission.MOH_ADMIN_WRITE,
            ApplicatioUserPermission.MOH_ADMIN_READ)),

    MOH_ADMIN(Sets.newHashSet(
            ApplicatioUserPermission.MOH_ADMIN_WRITE,
            ApplicatioUserPermission.HOSPITAL_ADMIN_READ,
            ApplicatioUserPermission.HOSPITAL_ADMIN_WRITE)),

    MOH_USER(Sets.newHashSet(
            ApplicatioUserPermission.HOSPITAL_ADMIN_READ)),

    HOSPITAL_ADMIN(Sets.newHashSet(
            ApplicatioUserPermission.HOSPITAL_USER_READ,
            ApplicatioUserPermission.HOSPITAL_USER_WRITE,
            ApplicatioUserPermission.PATIENT_READ,
            ApplicatioUserPermission.PATIENT_WRITE)),

    PATIENT(Sets.newHashSet(
            ApplicatioUserPermission.PATIENT_WRITE)),

    HOSPITAL_USER(Sets.newHashSet(
            ApplicatioUserPermission.PATIENT_READ, 
            ApplicatioUserPermission.PATIENT_WRITE));


    private final Set<ApplicatioUserPermission> permissions;

    ApplicationUserRole(Set<ApplicatioUserPermission> permissions) {
		this.permissions = permissions;
	}

	public Set<ApplicatioUserPermission> getPermissions() {
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
