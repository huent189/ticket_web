package uet.japit.k62.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uet.japit.k62.constant.MessageConstant;
import uet.japit.k62.constant.StatusCode;
import uet.japit.k62.exception.exception_define.detail.*;
import uet.japit.k62.models.request.ReqBookingSelectTicket;
import uet.japit.k62.models.response.data_response.ResBooking;
import uet.japit.k62.models.response.data_response.ResTicketClass;
import uet.japit.k62.models.response.http_response.HttpResponse;
import uet.japit.k62.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @GetMapping("/{event_id}/select-ticket")
    public ResponseEntity getByEvent(@PathVariable(value = "event_id") String event_id) {
        HttpResponse<List<ResTicketClass>> responseData = new HttpResponse<>(StatusCode.OK, MessageConstant.SUCCESS,
                bookingService.getTicketInfo(event_id));
        return ResponseEntity.ok(responseData);
    }
    @PostMapping("/{event_id}/select-ticket")
    public ResponseEntity selectTickets(@RequestBody ReqBookingSelectTicket reqSelectedTicket, @PathVariable(value = "event_id") String event_id) throws MaximumTicketExceeded, MinimumTicketNotReached, InvalidVoucherException, EventNotFoundException, VoucherNotFoundException, TicketNotFoundException {
        HttpResponse<ResBooking> response = new HttpResponse<>(StatusCode.OK, MessageConstant.SUCCESS,
                bookingService.selectTickets(reqSelectedTicket, event_id));
        return ResponseEntity.ok(response);
    }
}
