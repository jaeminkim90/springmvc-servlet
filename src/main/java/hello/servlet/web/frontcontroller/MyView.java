package hello.servlet.web.frontcontroller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

// 화면 이동을 담당
public class MyView {
    private String viewPath;

    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        modelToRequestAttribute(model, request);
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    // JSP가 request.getAttribute()로 데이터를 조회하기 때문에 모델에서 데이터를 꺼내 request.setAttribute 작업을 해야한다.
    private void modelToRequestAttribute(Map<String, Object> model, HttpServletRequest request) {

        // model은 단일 member 객체 또는 전체 Members가 담긴 List에 해당한다.
        model.forEach((key, value) -> request.setAttribute(key, value));
    }
}
