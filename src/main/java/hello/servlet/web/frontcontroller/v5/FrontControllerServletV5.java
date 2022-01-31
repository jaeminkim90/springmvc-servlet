package hello.servlet.web.frontcontroller.v5;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap(); // 전체 핸들러(컨트롤러) Mapping 정보를 필드 변수(Map)에 담는다.
        initHandlerAdapters(); // 전용 어댑터 객체를 필드 변수(List)에 담는다.
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());

        // V4 핸들러 어댑터 추가ㅣ
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        // V4 핸들러 추가
        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Handler Mapping Map에서 핸들러(컨트롤러)를 찾는다.
        Object handler = getHandler(request);
        if (handler == null) {
            // 없을 경우 404 처리
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 파라미터로 전달된 핸들러에 사용할 수 있는 어댑터를 찾아온다.
        MyHandlerAdapter adapter = getHandlerAdapter(handler);

        // 논리 이름이 담긴 ModelView 반환
        ModelView mv = adapter.handle(request, response, handler);

        // 논리 이름을 이용하여 viewResolver로 전체 viewPath를 가지고 있는 MyView 반환
        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);

        // render()를 통해 dispatcher.forward() 실행
        view.render(mv.getModel(), request, response);

    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {

        // supports()에 instanceof 조건을 이용하여 핸들러 사용에 필요한 어댑터를 찾는다.
        for (MyHandlerAdapter adapter : handlerAdapters) {
            // handlerAdapters를 모아놓은 List에서 같은 컨트롤러 인터페이스를 사용하는 어댑터 탐색
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        // 없을 경우 예외를 반환한다.
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler = " + handler);
    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }

    // MyView 객체 생성 후 반환(논리 경로를 물리 경로로 변경하여 생성자 파라미터에 이용)
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
