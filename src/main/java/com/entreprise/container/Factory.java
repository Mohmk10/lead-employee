package com.entreprise.container;

import com.entreprise.repository.AdminRepository;
import com.entreprise.repository.ManagerRepository;
import com.entreprise.repository.Impl.AdminRepositoryImpl;
import com.entreprise.repository.Impl.ManagerRepositoryImpl;
import com.entreprise.services.AdminService;
import com.entreprise.services.ManagerService;
import com.entreprise.services.Impl.AdminServiceImpl;
import com.entreprise.services.Impl.ManagerServiceImpl;
import com.entreprise.views.AdminView;
import com.entreprise.views.ManagerView;
import com.entreprise.views.SuperAdminView;
import com.entreprise.views.Impl.AdminViewImpl;
import com.entreprise.views.Impl.ManagerViewImpl;
import com.entreprise.views.Impl.SuperAdminViewImpl;

public final class Factory {

    private Factory() {}

    public static AdminRepository adminRepository() { return new AdminRepositoryImpl(); }
    public static ManagerRepository managerRepository() { return new ManagerRepositoryImpl(); }

    public static AdminService adminService() { return new AdminServiceImpl(adminRepository()); }
    public static ManagerService managerService() { return new ManagerServiceImpl(managerRepository()); }

    public static AdminView adminView() { return new AdminViewImpl(adminService()); }
    public static ManagerView managerView() { return new ManagerViewImpl(managerService()); }
    public static SuperAdminView superAdminView() { return new SuperAdminViewImpl(adminService(), adminView(), managerView()); }
}
