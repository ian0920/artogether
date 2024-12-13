package com.artogether.aop;

import com.artogether.common.permission.PermissionAnn;
import com.artogether.common.permission.PermissionService;
import com.artogether.common.system_manager.SystemManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Aspect
@Component
public class PermissionAop {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private PermissionService permissionService;


//    @Before("@annotation(permissionAnn)")  // 攔截所有方法
//    public void permBefore(PermissionAnn permissionAnn) {
//        HttpSession session = request.getSession();
//        System_manager manager = new System_manager();
//        // 需要權限
//        Integer usePerm = Integer.parseInt(permissionAnn.value());
//        // 獲取管理員權限
//        List<Integer> hasPerm = permissionService.findByDescId(manager);
//        if (hasPerm.contains(usePerm)) {
//
//        }
//    }

    @Around("@annotation(permissionAnn)")  // Intercept all methods annotated with PermissionAnn
    public Object permBefore(ProceedingJoinPoint joinPoint, PermissionAnn permissionAnn) throws Throwable {
        HttpSession session = request.getSession();// Get the current session
        Integer managerId = (Integer) session.getAttribute("managerId");

          // 放資料測試
//        SystemManager manager = new SystemManager();
//            manager.setId(1);
//            manager.setAccount("aaa");
//            manager.setPassword("aaa");
//            manager.setPhone("000");

        SystemManager manager = permissionService.findManagerById(managerId); // 假設你有這個方法
        // 因為這個人不存在permission這張table 所以這個人也不會有任何權限
        if (manager == null) {
            // 資料庫中沒有該用戶，跳轉到登錄錯誤頁面
            request.setAttribute("errorMessage", "User not found in the database.");
            request.getRequestDispatcher("/error/login").forward(request, response);
            return null;  // 阻止繼續執行後面的代碼
        }

//        if (manager == null) {
//            throw new IllegalStateException("User not logged in");
//        }

        // permission可能是1,2,3,4,5,6 要先把字串裝入List
        Integer requiredPerm = Integer.parseInt(permissionAnn.value());
        System.out.println("需要權限 " + requiredPerm);

        List<Integer> userPermissions = permissionService.findByDescId(manager);
        System.out.println("擁有權限 " + userPermissions);

        if (!userPermissions.contains(requiredPerm)) {
            System.out.println("no permission"); // 顯示沒有權限
            response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 回傳錯誤
            return response; // 傳回404頁面
        }

        return joinPoint.proceed(); // 如果確實有權限就繼續執行進入頁面
    }

}
