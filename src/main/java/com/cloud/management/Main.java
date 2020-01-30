package com.cloud.management;

import com.cloud.management.config.Properties;
import com.cloud.management.management.CloudManagement;

public class Main {
    public static void main(String[] args) {
        System.out.println("************************************************************************");
        System.out.println("***                                START                             ***");
        System.out.println("************************************************************************");
        System.out.println("Program will generate " + Properties.getInstance().getDataInputs().size() + " summaries");
        System.out.println("************************************************************************\n");

        CloudManagement service = new CloudManagement();
        service.run();
    }
}
