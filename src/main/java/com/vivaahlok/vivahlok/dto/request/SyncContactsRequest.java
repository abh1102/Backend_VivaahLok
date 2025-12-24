package com.vivaahlok.vivahlok.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SyncContactsRequest {
    private List<ContactItem> contacts;
    
    @Data
    public static class ContactItem {
        private String name;
        private String phone;
    }
}
