package com.hospitalmanagementsystem.demo.services;

import com.hospitalmanagementsystem.demo.entities.Doctor;
import com.hospitalmanagementsystem.demo.entities.DoctorSchedule;
import com.hospitalmanagementsystem.demo.entities.User;
import com.hospitalmanagementsystem.demo.exceptions.DoctorException;
import com.hospitalmanagementsystem.demo.repositories.DoctorRepository;
import com.hospitalmanagementsystem.demo.repositories.DoctorScheduleRepository;
import com.hospitalmanagementsystem.demo.repositories.UserRepository;
import com.hospitalmanagementsystem.demo.requests.CompleteDoctorScheduleRequest;
import com.hospitalmanagementsystem.demo.responses.CompleteDoctorScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final DoctorRepository doctorRepository;

    private final DoctorScheduleRepository doctorScheduleRepository;

    private final UserRepository userRepository;

    //complete doctor schedule
    public CompleteDoctorScheduleResponse completeDoctorScheduleResponse(CompleteDoctorScheduleRequest req, Long doctorId) throws DoctorException {

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

}
