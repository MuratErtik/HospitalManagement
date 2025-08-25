package com.hospitalmanagementsystem.demo.services;

import com.hospitalmanagementsystem.demo.entities.AppointmentSlot;
import com.hospitalmanagementsystem.demo.entities.Doctor;
import com.hospitalmanagementsystem.demo.entities.DoctorSchedule;
import com.hospitalmanagementsystem.demo.entities.User;
import com.hospitalmanagementsystem.demo.exceptions.AppointmentException;
import com.hospitalmanagementsystem.demo.exceptions.DoctorException;
import com.hospitalmanagementsystem.demo.repositories.AppointmentSlotRepository;
import com.hospitalmanagementsystem.demo.repositories.DoctorRepository;
import com.hospitalmanagementsystem.demo.repositories.DoctorScheduleRepository;
import com.hospitalmanagementsystem.demo.repositories.UserRepository;
import com.hospitalmanagementsystem.demo.requests.CompleteDoctorScheduleRequest;
import com.hospitalmanagementsystem.demo.requests.CreateSlotRequest;
import com.hospitalmanagementsystem.demo.responses.CompleteDoctorScheduleResponse;
import com.hospitalmanagementsystem.demo.responses.CreateAppointmentSlotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final DoctorRepository doctorRepository;

    private final DoctorScheduleRepository doctorScheduleRepository;

    private final UserRepository userRepository;

    private final AppointmentSlotRepository appointmentSlotRepository;

    //complete doctor schedule
    public CompleteDoctorScheduleResponse generateDoctorScheduleResponse(CompleteDoctorScheduleRequest req, Long doctorId) throws DoctorException {

        User user = userRepository.findByUserId(doctorId);

        if(user == null) throw new DoctorException("user not found with id "+doctorId);


        Doctor doctor = doctorRepository.findDoctorByUser(user);

        if(doctor == null) throw new DoctorException("Doctor not found with id "+doctorId);

        DoctorSchedule doctorSchedule = new DoctorSchedule();

        doctorSchedule.setDoctor(doctor);

        doctorSchedule.setDayOfWeek(req.getDayOfWeek());

        doctorSchedule.setStartTime(req.getStartTime());

        doctorSchedule.setEndTime(req.getEndTime());

        doctorSchedule.setSlotDurationMinutes(req.getSlotDurationMinutes());

        doctorScheduleRepository.save(doctorSchedule);

        CompleteDoctorScheduleResponse response = new CompleteDoctorScheduleResponse();

        response.setDoctorId(doctorId);

        response.setMessage("Successfully completed Doctor Schedule.\nDoctor Name:"+doctor.getUser().getName()+"\nDoctor Off date:"+req.getStartTime()+"\nDoctor End date:"+req.getEndTime()
        +"\nDoctor Schedule Duration Minutes:"+req.getSlotDurationMinutes());

        return response;

    }

    public CreateAppointmentSlotResponse createAppointmentSlot(CreateSlotRequest req, Long doctorId) throws DoctorException,AppointmentException {


        User user = userRepository.findByUserId(doctorId);

        if(user == null) throw new DoctorException("user not found with id "+doctorId);

        Doctor doctor = doctorRepository.findDoctorByUser(user);

        if(doctor == null) throw new DoctorException("Doctor not found with id "+doctorId);

        DoctorSchedule doctorSchedule = doctorScheduleRepository.findDoctorScheduleByDoctor(doctor);

        if(doctorSchedule == null) throw new DoctorException("Doctor Schedule has not found with id "+doctorId);

        DayOfWeek doctorOffDayOfWeek = doctorSchedule.getDayOfWeek();


        for (LocalDate date = req.getStartDate();!date.isAfter(req.getEndDate());date = date.plusDays(1)) {

            DayOfWeek day = date.getDayOfWeek();

            if(day== DayOfWeek.SATURDAY || day==DayOfWeek.SUNDAY || day==doctorOffDayOfWeek){
                continue;
            }

            generateSlotForDay(doctorSchedule,date);

        }

        CreateAppointmentSlotResponse response = new CreateAppointmentSlotResponse();

        response.setMessage("Successfully created appointment slots");

        return response;

    }

    private void generateSlotForDay(DoctorSchedule doctorSchedule, LocalDate date)throws AppointmentException {
        final int duration = doctorSchedule.getSlotDurationMinutes();

        LocalTime time = doctorSchedule.getStartTime();
        final LocalTime dayEnd = doctorSchedule.getEndTime();

        final LocalTime lunchStart = LocalTime.NOON;        // 12:00
        final LocalTime lunchEnd   = LocalTime.of(13, 0);   // 13:00

        while (!time.plusMinutes(duration).isAfter(dayEnd)) {

            // Lunch break skip
            boolean overlapsLunch = time.isBefore(lunchEnd) && time.plusMinutes(duration).isAfter(lunchStart);
            if (overlapsLunch) {
                time = lunchEnd;
                continue;
            }

            LocalDateTime newStartTime = LocalDateTime.of(date, time);
            LocalDateTime newEndTime   = newStartTime.plusMinutes(duration);

            // Overlap Control: existing.start < newEnd && existing.end > newStart
            boolean overlap = appointmentSlotRepository
                    .existsByDoctorScheduleAndStartTimeLessThanAndEndTimeGreaterThan(
                            doctorSchedule,
                            newEndTime,
                            newStartTime
                    );

            if (overlap) {
                throw new AppointmentException("Overlap detected for slot starting at " + newStartTime);
            }

            // Check duplicate start
            boolean sameStartExists =
                    appointmentSlotRepository.existsByDoctorScheduleAndStartTime(doctorSchedule, newStartTime);

            if (sameStartExists) {
                throw new AppointmentException("Slot already exists for start time " + newStartTime);
            }

            // If no conflicts, create new slot
            AppointmentSlot slot = new AppointmentSlot();
            slot.setDoctorSchedule(doctorSchedule);
            slot.setStartTime(newStartTime);
            slot.setEndTime(newEndTime);
            slot.setBooked(false);

            appointmentSlotRepository.save(slot);



            time = time.plusMinutes(duration);
        }
    }


}
