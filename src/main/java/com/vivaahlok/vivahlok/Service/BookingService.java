package com.vivaahlok.vivahlok.service;

import com.vivaahlok.vivahlok.dto.BookingDTO;
import com.vivaahlok.vivahlok.dto.request.CreateBookingRequest;
import com.vivaahlok.vivahlok.dto.request.UpdateBookingRequest;
import com.vivaahlok.vivahlok.dto.request.UpdateBookingStatusRequest;
import com.vivaahlok.vivahlok.dto.response.PageResponse;
import com.vivaahlok.vivahlok.entity.Booking;
import com.vivaahlok.vivahlok.entity.Vendor;
import com.vivaahlok.vivahlok.exception.BadRequestException;
import com.vivaahlok.vivahlok.exception.ResourceNotFoundException;
import com.vivaahlok.vivahlok.repository.BookingRepository;
import com.vivaahlok.vivahlok.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final VendorRepository vendorRepository;
    
    public PageResponse<BookingDTO> getUserBookings(String userId, String status, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Booking> bookingPage;
        
        if (status != null && !status.isEmpty()) {
            bookingPage = bookingRepository.findByUserIdAndStatus(userId, status, pageable);
        } else {
            bookingPage = bookingRepository.findByUserId(userId, pageable);
        }
        
        List<BookingDTO> bookings = bookingPage.getContent().stream()
                .map(this::mapToBookingDTO)
                .collect(Collectors.toList());
        
        return PageResponse.<BookingDTO>builder()
                .content(bookings)
                .total(bookingPage.getTotalElements())
                .page(page)
                .pages(bookingPage.getTotalPages())
                .build();
    }
    
    public BookingDTO getBookingById(String id, String userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        if (!booking.getUserId().equals(userId)) {
            throw new BadRequestException("Access denied");
        }
        
        return mapToBookingDTO(booking);
    }
    
    public String createBooking(String userId, CreateBookingRequest request) {
        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        
        Booking booking = Booking.builder()
                .userId(userId)
                .vendorId(request.getVendorId())
                .vendorName(vendor.getName())
                .eventDate(request.getEventDate())
                .eventTime(request.getEventTime())
                .services(request.getServices())
                .notes(request.getNotes())
                .amount(request.getAmount())
                .status("pending")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        booking = bookingRepository.save(booking);
        return booking.getId();
    }
    
    public void updateBooking(String id, String userId, UpdateBookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        if (!booking.getUserId().equals(userId)) {
            throw new BadRequestException("Access denied");
        }
        
        if (request.getEventDate() != null) booking.setEventDate(request.getEventDate());
        if (request.getEventTime() != null) booking.setEventTime(request.getEventTime());
        if (request.getNotes() != null) booking.setNotes(request.getNotes());
        
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);
    }
    
    public void cancelBooking(String id, String userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        if (!booking.getUserId().equals(userId)) {
            throw new BadRequestException("Access denied");
        }
        
        booking.setStatus("cancelled");
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);
    }
    
    public void updateBookingStatus(String id, String userId, UpdateBookingStatusRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        if (!booking.getUserId().equals(userId)) {
            throw new BadRequestException("Access denied");
        }
        
        booking.setStatus(request.getStatus());
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);
    }
    
    private BookingDTO mapToBookingDTO(Booking booking) {
        return BookingDTO.builder()
                .id(booking.getId())
                .vendorId(booking.getVendorId())
                .vendorName(booking.getVendorName())
                .eventDate(booking.getEventDate())
                .eventTime(booking.getEventTime())
                .services(booking.getServices())
                .notes(booking.getNotes())
                .amount(booking.getAmount())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
