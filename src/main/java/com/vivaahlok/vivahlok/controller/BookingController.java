package com.vivaahlok.vivahlok.controller;

import com.vivaahlok.vivahlok.dto.BookingDTO;
import com.vivaahlok.vivahlok.dto.request.CreateBookingRequest;
import com.vivaahlok.vivahlok.dto.request.UpdateBookingRequest;
import com.vivaahlok.vivahlok.dto.request.UpdateBookingStatusRequest;
import com.vivaahlok.vivahlok.dto.response.ApiResponse;
import com.vivaahlok.vivahlok.dto.response.PageResponse;
import com.vivaahlok.vivahlok.security.CurrentUser;
import com.vivaahlok.vivahlok.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Booking APIs")
public class BookingController {
    
    private final BookingService bookingService;
    
    @GetMapping
    @Operation(summary = "Get user's bookings")
    public ResponseEntity<PageResponse<BookingDTO>> getUserBookings(
            @CurrentUser String userId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page) {
        PageResponse<BookingDTO> response = bookingService.getUserBookings(userId, status, page);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get booking details")
    public ResponseEntity<BookingDTO> getBookingById(
            @PathVariable String id,
            @CurrentUser String userId) {
        BookingDTO booking = bookingService.getBookingById(id, userId);
        return ResponseEntity.ok(booking);
    }
    
    @PostMapping
    @Operation(summary = "Create new booking")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createBooking(
            @CurrentUser String userId,
            @Valid @RequestBody CreateBookingRequest request) {
        String bookingId = bookingService.createBooking(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Booking created successfully", 
                Map.of("bookingId", bookingId, "status", "pending")));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update booking")
    public ResponseEntity<ApiResponse<Void>> updateBooking(
            @PathVariable String id,
            @CurrentUser String userId,
            @Valid @RequestBody UpdateBookingRequest request) {
        bookingService.updateBooking(id, userId, request);
        return ResponseEntity.ok(ApiResponse.success("Booking updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel booking")
    public ResponseEntity<ApiResponse<Void>> cancelBooking(
            @PathVariable String id,
            @CurrentUser String userId) {
        bookingService.cancelBooking(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully"));
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update booking status")
    public ResponseEntity<ApiResponse<Void>> updateBookingStatus(
            @PathVariable String id,
            @CurrentUser String userId,
            @Valid @RequestBody UpdateBookingStatusRequest request) {
        bookingService.updateBookingStatus(id, userId, request);
        return ResponseEntity.ok(ApiResponse.success("Booking status updated successfully"));
    }
}
