package vn.edu.fpt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.fpt.dto.transaction_manager.TransactionCountByStatusDTO;
import vn.edu.fpt.dto.transaction_manager.TransactionDetailDTO;
import vn.edu.fpt.dto.transaction_manager.TransactionListDTO;
import vn.edu.fpt.enums.PaymentStatus;
import vn.edu.fpt.service.ManagerDashboardService;
import vn.edu.fpt.service.payment.PaymentService;
import vn.edu.fpt.util.AppConstants;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerTransactionController {
    private final PaymentService paymentService;

    @GetMapping("/transaction-history/list")
    public String showTransaction(Model model, @RequestParam(required = false) String status, @RequestParam(required = false) LocalDate fromDate, @RequestParam(required = false) LocalDate toDate, @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int page) {

        TransactionCountByStatusDTO transactionCountByStatusDTO = paymentService.gettransactionCountByStatusDTO();
        Integer totalTransaction = transactionCountByStatusDTO.getAllTransaction();

        Page<TransactionListDTO> pageTransaction = paymentService.getTransactionByFilter(status, (fromDate == null ? null : fromDate.atStartOfDay()), (toDate == null ? null : toDate.plusDays(1).atStartOfDay()), keyword, page);

        int startPage = 0;
        int endPage = 0;
        if (pageTransaction.getTotalPages() > 0) {
            startPage = (pageTransaction.getNumber() / AppConstants.NUMBER_PAGE_PER_BLOCK) * AppConstants.NUMBER_PAGE_PER_BLOCK;
            endPage = Math.min(startPage + AppConstants.NUMBER_PAGE_PER_BLOCK - 1, pageTransaction.getTotalPages() - 1);
        }

        model.addAttribute("status", status);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("keyword", keyword);

        model.addAttribute("statuses", PaymentStatus.values());
        model.addAttribute("pageTransaction", pageTransaction);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("transactionCountByStatusDTO", transactionCountByStatusDTO);
        model.addAttribute("totalTransaction", totalTransaction);
        return "manager/transaction-history/transaction-history";
    }

    @GetMapping("/transaction-detail/{paymentId}")
    public String getTransactionDetail(@PathVariable(name = "paymentId") Integer paymentId, Model model) {
        TransactionDetailDTO transactionDetailDTO = paymentService.getTransactionDetailByPaymentId(paymentId);
        model.addAttribute("transactionDetail", transactionDetailDTO);
        return "manager/transaction-history/detail-transaction";
    }
}
