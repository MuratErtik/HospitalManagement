package com.hospitalmanagementsystem.demo.services;

import com.hospitalmanagementsystem.demo.entities.*;
import com.hospitalmanagementsystem.demo.exceptions.AppointmentException;
import com.hospitalmanagementsystem.demo.exceptions.DoctorException;
import com.hospitalmanagementsystem.demo.repositories.*;
import com.hospitalmanagementsystem.demo.requests.AppointmentNoteRequest;
import com.hospitalmanagementsystem.demo.requests.CompleteDoctorScheduleRequest;
import com.hospitalmanagementsystem.demo.requests.CreateSlotRequest;
import com.hospitalmanagementsystem.demo.requests.PatientSlotFilterRequest;
import com.hospitalmanagementsystem.demo.responses.*;
import com.hospitalmanagementsystem.demo.specifications.AppointmentSlotSpecifications;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final DoctorRepository doctorRepository;

    private final DoctorScheduleRepository doctorScheduleRepository;

    private final UserRepository userRepository;

    private final AppointmentSlotRepository appointmentSlotRepository;

    private final AppointmentSlotSpecifications appointmentSlotSpecifications;

    private final DepartmentRepository departmentRepository;

    private final PatientRepository patientRepository;

    private final AppointmentStatusRepository appointmentStatusRepository;

    private final EmailService emailService;

    private final AppointmentRepository appointmentRepository;


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

    public List<AppointmentSlotToPatientResponse> getFilteredSlots(PatientSlotFilterRequest request) {
        List<AppointmentSlot> slots = appointmentSlotRepository.findAll(
                AppointmentSlotSpecifications.byFilters(request)
        );

        return slots.stream().map(this::mapToPatientSlotResponse).collect(Collectors.toList());
    }

    private AppointmentSlotToPatientResponse mapToPatientSlotResponse(AppointmentSlot appointmentSlot) {
        Doctor doctor = appointmentSlot.getDoctorSchedule().getDoctor();
        Department department = doctor.getDepartment();

        return new AppointmentSlotToPatientResponse(
                appointmentSlot.getStartTime().toLocalDate(),
                appointmentSlot.getStartTime().toLocalTime(),
                appointmentSlot.getEndTime().toLocalTime(),
                doctor.getUser().getName() + " " + doctor.getUser().getLastname(),
                department.getDepartmentName()
        );
    }

    public List<DepartmentResponse> getDepartments() {

        List<Department> departments = departmentRepository.findAll();

        return departments.stream().map(this::mapDepartmentResponse).collect(Collectors.toList());


    }

    private DepartmentResponse mapDepartmentResponse(Department department) {

        return new DepartmentResponse(
                department.getDepartmentId(),
                department.getDepartmentName()
        );
    }

    public List<DoctorResponse> getDoctors(Long departmentId) {

        Department department = departmentRepository.findByDepartmentId(departmentId);

        if (department == null) {
            throw new AppointmentException("Department with id " + departmentId + " not found");
        }

        List<Doctor> doctors = doctorRepository.findDoctorByDepartment(department);

        return doctors.stream().map(this::mapDoctorResponse).collect(Collectors.toList());


    }

    private DoctorResponse mapDoctorResponse(Doctor doctor) {

        return new DoctorResponse(
                doctor.getDoctorId(),
                doctor.getUser().getName(),
                doctor.getUser().getLastname()

        );
    }

    //make an appointment
    public MakeAppointmentResponse makeAppointment(Long appointmentSlotId,Long patientId) throws AppointmentException, MessagingException {

        AppointmentSlot appointmentSlot = appointmentSlotRepository.findAppointmentSlotBySlotId(appointmentSlotId);

        if (appointmentSlot == null) {
            throw new AppointmentException("Slot with id " + appointmentSlotId + " not found");
        }

        if (appointmentSlot.isBooked()){
            throw new AppointmentException("Slot is already booked");
        }

        appointmentSlot.setBooked(true);

        Appointment appointment = new Appointment();

        appointment.setSlot(appointmentSlot);

        User user = userRepository.findByUserId(patientId);

        if (user == null) {
            throw new AppointmentException("User with id " + patientId + " not found");
        }

        Patient patient = patientRepository.findPatientByUser(user);

        if (patient == null) {
            throw new AppointmentException("Patient with id " + patientId + " not found");
        }

        appointment.setPatient(patient);

        AppointmentStatus appointmentStatus = appointmentStatusRepository.findByAppointmentStatus("BOOKED");

        appointment.setStatus(appointmentStatus);

        appointmentRepository.save(appointment);

        MakeAppointmentResponse makeAppointmentResponse = new MakeAppointmentResponse();

        makeAppointmentResponse.setMessage("Successfully maked appointment");

        emailService.makeAppointment(user.getEmail(),user.getName(),user.getLastname(),appointment);

        return makeAppointmentResponse;

    }

    public MakeAppointmentResponse addNote(Long appointmentId, Long patientId, AppointmentNoteRequest request){

        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId);

        if (appointment == null) {
            throw new AppointmentException("Appointment with id " + appointmentId + " not found");
        }

        appointment.setNotes(request.getNote());

        MakeAppointmentResponse makeAppointmentResponse = new MakeAppointmentResponse();

        makeAppointmentResponse.setMessage("Successfully added appointment' note.");

        appointmentRepository.save(appointment);

        return makeAppointmentResponse;
    }

    public MakeAppointmentResponse changeAppointmentStatus(Long appointmentId, Long userId,Long statusId){

        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId);

        if (appointment == null) {
            throw new AppointmentException("Appointment with id " + appointmentId + " not found");
        }

        AppointmentStatus appointmentStatus = appointmentStatusRepository.findByAppointmentStatusId(statusId);

        if (appointmentStatus == null) {
            throw new AppointmentException("Appointment status with id " + statusId + " not found");
        }

        AppointmentSlot appointmentSlot = appointment.getSlot();

        if (appointmentStatus.getAppointmentStatus().equals("CANCELLED")) {

            appointmentSlot.setBooked(false);
            appointmentSlotRepository.save(appointmentSlot);

        }

        appointment.setStatus(appointmentStatus);

        appointmentRepository.save(appointment);

        MakeAppointmentResponse makeAppointmentResponse = new MakeAppointmentResponse();

        makeAppointmentResponse.setMessage("Successfully changed appointment' status to " + appointmentStatus.getAppointmentStatus());

        return makeAppointmentResponse;

    }

    public List<AppointmentStatusResponse> getAllAppointmentStatus() {

        List<AppointmentStatus> appointmentStatuses = appointmentStatusRepository.findAll();

        return appointmentStatuses.stream().map(this::mapAppointmentStatusResponse).collect(Collectors.toList());
    }

    public AppointmentStatusResponse mapAppointmentStatusResponse(AppointmentStatus appointmentStatus) {

        return new AppointmentStatusResponse(appointmentStatus.getAppointmentStatusId(),appointmentStatus.getAppointmentStatus());
    }






}
