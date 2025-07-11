package com.oppenablers.jobhub.model;

import java.util.HashMap;

public class Interests {

    private static final HashMap<Integer, String> computerScienceMap = new HashMap<>();
    private static final HashMap<Integer, String> medicineMap = new HashMap<>();

    static {
        computerScienceMap.put(0, "Analyst");
        computerScienceMap.put(1, "Project Manager");
        computerScienceMap.put(2, "Designer");
        computerScienceMap.put(3, "Engineer");
        computerScienceMap.put(4, "Scientist");
        computerScienceMap.put(5, "Technician");
        computerScienceMap.put(6, "Developer");
        computerScienceMap.put(7, "Scrum");

        medicineMap.put(8, "Therapist");
        medicineMap.put(9, "Physician");
        medicineMap.put(10, "Dentist");
        medicineMap.put(11, "Pharmacist");
        medicineMap.put(12, "Medical Assistant");
        medicineMap.put(13, "Veterinarian");
        medicineMap.put(14, "Doctor");
        medicineMap.put(15, "Nurse");
    }

    public static HashMap<Integer, String> getComputerScienceMap() {
        return computerScienceMap;
    }

    public static HashMap<Integer, String> getMedicineMap() {
        return medicineMap;
    }
}
