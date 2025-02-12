package com.example.assignment_pedro_silva.controller;

import com.example.assignment_pedro_silva.dto.ReservationDTO;
import com.example.assignment_pedro_silva.exceptions.BookNotAvailableException;
import com.example.assignment_pedro_silva.exceptions.ExceededNumberOfReservationsException;
import com.example.assignment_pedro_silva.model.User;
import com.example.assignment_pedro_silva.service.ReservationService;
import com.example.assignment_pedro_silva.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
@Tag(name = "Reservations", description = "Book reservation management")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;
    private final User user;

    public ReservationController(ReservationService reservationService, UserService userService) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.user = userService.save(new User());
    }

    @GetMapping
    @Operation(summary = "Get all reservations", description = "Get a list with all reservations")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reservations retrieved successfully")
    public ResponseEntity<ApiResponse<List<ReservationDTO>>> getAll() {
        List<ReservationDTO> reservations = this.reservationService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Reservations retrieved successfully.", reservations));
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Get a reservation", description = "Get a reservation given an uuid")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reservation retrieved successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Reservation not found")
    public ResponseEntity<ApiResponse<ReservationDTO>> getByReservationId(@PathVariable String uuid) {
        ReservationDTO reservationDTO = this.reservationService.findReservation(UUID.fromString(uuid));
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Reservation retrieved successfully.", reservationDTO));
    }

    @PatchMapping("/cancel/{uuid}")
    @Operation(summary = "Cancel a reservation", description = "Change the status of a reservation to CANCELLED")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Reservation cancelled successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Reservation not found")
    public ResponseEntity<ApiResponse<ReservationDTO>> cancelReservation(@PathVariable String uuid) {
        reservationService.cancelReservation(UUID.fromString(uuid));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse<>("Reservation cancelled successfully.", null));
    }

    @PostMapping
    @Operation(summary = "Create a reservation", description = "Create a reservation of a book")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Reservation created successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "User cannot create another reservation or the book doesn't have available copies")
    public ResponseEntity<ApiResponse<ReservationDTO>> createReservation(@RequestBody ReservationDTO reservationDTO) throws BookNotAvailableException, ExceededNumberOfReservationsException {
        ReservationDTO createdReservationDTO = this.reservationService.saveReservation(reservationDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Reservation created successfully.", createdReservationDTO));
    }

    @GetMapping("/user/{uuid}")
    @Operation(summary = "Get all reservations from a user", description = "Get a list with all reservations given an user uuid")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reservations retrieved successfully")
    public ResponseEntity<ApiResponse<List<ReservationDTO>>> getAllReservationsByUserUuid(@PathVariable String uuid) {
        List<ReservationDTO> reservations = this.reservationService.findByUserUuid(UUID.fromString(uuid));
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Reservations retrieved successfully.", reservations));
    }
}
