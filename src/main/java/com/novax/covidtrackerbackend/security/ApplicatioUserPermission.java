package com.novax.covidtrackerbackend.security;


public enum ApplicatioUserPermission {
    
    MOH_ADMIN_READ("moh_admin:read"),
    MOH_ADMIN_WRITE("moh_admin:write"), 
    MOH_USER_READ("moh_user:read"),
    MOH_USER_WRITE("moh_user:write"), 
    HOSPITAL_ADMIN_READ("hospital_admin:read"),
    HOSPITAL_ADMIN_WRITE("hospital_admin:write"), 
    PATIENT_READ("patient:read"),
    PATIENT_WRITE("patient:write"), 
    HOSPITAL_USER_READ("hospital_user:read"),
    HOSPITAL_USER_WRITE("hospital_user:write");

    private final String permission;

    ApplicatioUserPermission(String permission) {
		this.permission = permission;
	}
    
    public String getPermission(){
        return this.permission;
    }

    
}
