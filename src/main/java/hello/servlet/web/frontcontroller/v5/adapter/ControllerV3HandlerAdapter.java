package hello.servlet.web.frontcontroller.v5.adapter;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v5.MyHandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerV3HandlerAdapter implements MyHandlerAdapter {
    @Override
    public boolean supports(Object handler) {

        // MemberFormControllerV3가 ControllerV3의 인스턴스인지 확인
        return (handler instanceof ControllerV3);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        // MemberFormControllerV3를 ControllerV3로 캐스팅
        ControllerV3 controller = (ControllerV3) handler;

        // 요청 파라미터 내용을 Map에 담는다
        Map<String, String> paramMap = creatParamMap(request);

        // 컨트롤러의 process()를 이용하여 논리 이름이 담긴 ModelView를 얻는다
        // MemberFormControllerV3.process가 호출된다
        ModelView mv = controller.process(paramMap);

        return mv;
    }

    private Map<String, String> creatParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
