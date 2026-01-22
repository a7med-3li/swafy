package com.swafy.common.util;

import com.swafy.user.dto.UserResponse;
import com.swafy.user.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

public class Helpers {
    public static void copyNonNullProperties(Object src, Object target) {
        BeanWrapper srcWrap = new BeanWrapperImpl(src);
        java.beans.PropertyDescriptor[] pds = srcWrap.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = srcWrap.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] ignoreProperties = emptyNames.toArray(new String[0]);
        BeanUtils.copyProperties(src, target, ignoreProperties);
    }

    public static UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .displayName(user.getFirstName() + " " + user.getLastName().charAt(0) + ".")
                .phoneNumber(maskPhoneNumber(user.getPhoneNumber()))
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .deleted(user.isDeleted())
                .gender(user.getGender())
                .build();
    }

    private static String maskPhoneNumber(String phone) {
        if (phone.length() > 4) {
            return phone.substring(0, 2) + "******" + phone.substring(phone.length() - 3);
        }
        return phone;
    }

}
