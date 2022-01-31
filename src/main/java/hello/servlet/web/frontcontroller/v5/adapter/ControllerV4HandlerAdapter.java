package hello.servlet.web.frontcontroller.v5.adapter;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v4.ControllerV4;
import hello.servlet.web.frontcontroller.v5.MyHandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerV4HandlerAdapter implements MyHandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ControllerV4);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {

        ControllerV4 controller = (ControllerV4) handler;

        // 요청 파라미터 내용을 Map에 담는다
        Map<String, String> paramMap = creatParamMap(request);

        // member나 members 데이터를 담는 model map
        HashMap<String, Object> model = new HashMap<>();

        // V4 process는 paramMap과 model(Map)을 파라미터로 받는다
        // 핸들러마다 가지고 있는 고유한 viewName이 반환된다.
        // model에 필요한 데이터는 controller에서 처리한다.
        String viewName = controller.process(paramMap, model);

        // 리턴 타입이 ModelView이므로, 반환할 ModelView 객체를 세팅한다.
        ModelView mv = new ModelView(viewName);
        mv.setModel(model);

        return mv;
    }

    private Map<String, String> creatParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
