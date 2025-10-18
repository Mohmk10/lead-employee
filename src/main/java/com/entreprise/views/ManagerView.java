package com.entreprise.views;

import com.entreprise.entity.Employee;

public interface ManagerView extends View<Employee> {
    void menuPrincipal(long managerId);
}
