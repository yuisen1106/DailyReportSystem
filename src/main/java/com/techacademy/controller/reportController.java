package com.techacademy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.service.EmployeeService;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("reports")
public class reportController {

   private final ReportService reportService;

    @Autowired
   public reportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 日報一覧画面
    @GetMapping("/list")
    public String list(Model model,@AuthenticationPrincipal UserDetail userDetail) {

    	String code = userDetail.getUsername();
    	System.out.println(code);
    	if("1".equals(code)) {
        model.addAttribute("listSize", reportService.findAll().size());
        model.addAttribute("reportList", reportService.findAll());
        }else {
        model.addAttribute("listSize", reportService.findAll(code).size());
        model.addAttribute("reportList", reportService.findAll(code));
        }
        return "reports/list";
    }

    // 日報詳細画面
    @GetMapping("/{id}/")
    public String detail(@PathVariable("id") Integer id, Model model) {


       model.addAttribute("report", reportService.findById(id));
        return "reports/detail";
    }

    // 従業員新規登録画面
    @GetMapping("/add")
    public String create(@ModelAttribute Report report,@AuthenticationPrincipal UserDetail userDetail,Model model) {

    	String employeeCode = userDetail.getUsername();
        model.addAttribute("username", reportService.employeeName(employeeCode));

        return "reports/new";
    }

   /* // 従業員新規登録処理
    @PostMapping(value = "/add")
    public String add(@Validated Employee employee, BindingResult res, Model model) {

        // パスワード空白チェック
        /*
         * エンティティ側の入力チェックでも実装は行えるが、更新の方でパスワードが空白でもチェックエラーを出さずに
         * 更新出来る仕様となっているため上記を考慮した場合に別でエラーメッセージを出す方法が簡単だと判断
         */
     /*   if ("".equals(employee.getPassword())) {
            // パスワードが空白だった場合
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.BLANK_ERROR),
                    ErrorMessage.getErrorValue(ErrorKinds.BLANK_ERROR));

            return create(employee);

        }

        // 入力チェック
        if (res.hasErrors()) {
            return create(employee);
        }

        // 論理削除を行った従業員番号を指定すると例外となるためtry~catchで対応
        // (findByIdでは削除フラグがTRUEのデータが取得出来ないため)
        try {
            ErrorKinds result = employeeService.save(employee);

            if (ErrorMessage.contains(result)) {
                model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
                return create(employee);
            }

        } catch (DataIntegrityViolationException e) {
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DUPLICATE_EXCEPTION_ERROR),
                    ErrorMessage.getErrorValue(ErrorKinds.DUPLICATE_EXCEPTION_ERROR));
            return create(employee);
        }

        return "redirect:/employees";
    }

    // 従業員削除処理
    @PostMapping(value = "/{code}/delete")
    public String delete(@PathVariable String code, @AuthenticationPrincipal UserDetail userDetail, Model model) {

        ErrorKinds result = employeeService.delete(code, userDetail);

        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            model.addAttribute("employee", employeeService.findByCode(code));
            return detail(code, model);
        }

        return "redirect:/employees";
    }


    */
    //更新画面
    @GetMapping("/{id}/update")
    public String getReport(@PathVariable("id") Integer id,Model model,Report report) {

    if(id != null) {
    model.addAttribute("report" , reportService.findById(id));
    }else {
	model.addAttribute("report",report);}
    return "reports/update";



}

    /**更新処理*/

/*	@PostMapping("/update/{code}/")
	public String postEmpoyee(@Validated Employee employee,BindingResult res, Model model) {

		if(res.hasErrors()) {
			return getEmployee(null,model,employee);
		}
		employeeService.update(employee);

		ErrorKinds result = employeeService.update(employee);
		if (ErrorMessage.contains(result)) {
                model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
                return create(employee);
            }

		return "redirect:/employees";
	}
     //～追加

	*/
}
