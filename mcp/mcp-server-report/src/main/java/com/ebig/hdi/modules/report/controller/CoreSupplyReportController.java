package com.ebig.hdi.modules.report.controller;


import com.ebig.hdi.modules.sys.controller.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wenchao
 * @email
 * @date 2020-01-06 16:01:14
 */
@RestController
@RequestMapping("/core/coresupplymaster")
@Slf4j
public class CoreSupplyReportController extends AbstractController {

//    @Autowired
//    private CoreSupplyReportService CoreSupplyReportService;
//
//    @RequestMapping("/report")
//    public void report(@RequestBody Long id, HttpServletResponse response) {
//        String jrxmlPath = Thread.currentThread().getContextClassLoader().getResource("templates/coresupply_master.jrxml").getPath();
//        String jasperPath = Thread.currentThread().getContextClassLoader().getResource("templates/coresupply_master.jasper").getPath();
//        if (getUser() != null) {
//            Map<String, Object> parameters = new HashMap<>(16);
//            FileInputStream isRef = null;
//            ServletOutputStream sosRef = null;
//            try {
//                JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
//                isRef = new FileInputStream(new File(jasperPath));
//                sosRef = response.getOutputStream();
//                List<CoreSupplyDetailVo> list = CoreSupplyReportService.getListBySupplyMasterId(id);
//                JasperRunManager.runReportToPdfStream(isRef, sosRef, parameters, new JRBeanCollectionDataSource(list));
//                response.setCharacterEncoding("UTF-8");
//                response.reset();
//                response.setHeader("Access-Control-Allow-Origin", "*");
//                response.setHeader("Cache-Control", "no-cache");
//                response.setContentType("application/pdf");
//            } catch (Exception e) {
//                log.error(e.getMessage());
//            } finally {
//                try {
//                    if (sosRef != null) {
//                        sosRef.flush();
//                        sosRef.close();
//                    }
//                } catch (IOException e) {
//                    log.error(e.getMessage());
//                }
//                try {
//                    if (isRef != null) {
//                        isRef.close();
//                    }
//                } catch (IOException e) {
//                    log.error(e.getMessage());
//                }
//            }
//
//        }
//    }
}
