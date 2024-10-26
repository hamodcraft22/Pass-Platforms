package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingMemberDao;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static polytechnic.bh.PassPlatforms_Backend.Util.UsersService.getToken;

@Service
public class MailServ
{
//    @Autowired
//    private JavaMailSender mailSender;

    private final RestTemplate restTemplate = new RestTemplate();

    // async method to send invite emails upon booking
    // handles group sends as well
    // used when access to Microsoft is not available
//    @Async
//    public void sendInvite(BookingDao bookingDao)
//    {
//        try
//        {
//            // creating the initial message
//            MimeMessage message = mailSender.createMimeMessage();
//
//            // headers
//            message.addHeaderLine("method=REQUEST");
//            message.addHeaderLine("charset=UTF-8");
//            message.addHeaderLine("component=VEVENT");
//            message.setFrom(new InternetAddress("PASS.Platform@polytechnic.bh")); // TODO - MAKE ENV VAR
//
//            // send it to the main student
//            message.setRecipient(Message.RecipientType.TO, new InternetAddress(bookingDao.getStudent().getUserid() + "@student.polytechnic.bh"));
//
//            // send it to the leader
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(bookingDao.getSlot().getLeader().getUserid() + "@student.polytechnic.bh"));
//
//            // list of student email - if group
//            List<String> studentMembers = new ArrayList<>();
//
//            // if it is a group, send to them as well
//            if (bookingDao.getBookingMembers() != null && !bookingDao.getBookingMembers().isEmpty())
//            {
//                // header info
//                message.setSubject(bookingDao.getCourse().getCourseid() + " " + bookingDao.getCourse().getCoursename() + " - Group Booking");
//
//                for (BookingMemberDao student : bookingDao.getBookingMembers())
//                {
//                    studentMembers.add(student.getStudent().getUserid() + "@student.polytechnic.bh");
//                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(student.getStudent().getUserid() + "@student.polytechnic.bh"));
//                }
//            }
//            else
//            {
//                message.setSubject(bookingDao.getCourse().getCourseid() + " " + bookingDao.getCourse().getCoursename());
//            }
//
//
//            // calendar invite file
//            String calendarContent =
//                    "BEGIN:VCALENDAR\n" +
//                            "METHOD:REQUEST\n" +
//                            "PRODID:Microsoft Exchange Server 2010\n" +
//                            "VERSION:2.0\n" +
//                            "BEGIN:VTIMEZONE\n" +
//                            "TZID:Asia/Bahrain\n" +
//                            "END:VTIMEZONE\n" +
//                            "BEGIN:VEVENT\n" +
//                            "ORGANIZER;EMAIL="+ bookingDao.getSlot().getLeader().getUserid() +"@student.polytechnic.bh;ROLE=CHAIR:mailto:"+bookingDao.getSlot().getLeader().getUserid()+"@student.polytechnic.bh\n" +
//                            "ATTENDEE;" + bookingDao.getStudent().getUserName() + ";EMAIL=" + (bookingDao.getStudent().getUserid() + "@student.polytechnic.bh") + ";ROLE=REQ-PARTICIPANT:mailto:" + (bookingDao.getStudent().getUserid() + "@student.polytechnic.bh") + "\n";
//
//            if (!studentMembers.isEmpty())
//            {
//                for (String studentEmail : studentMembers)
//                {
//                    calendarContent += ("ATTENDEE;EMAIL=" + studentEmail + ";ROLE=REQ-PARTICIPANT:mailto:" + studentEmail + "\n");
//                }
//            }
//
//            calendarContent +=
//                    "DESCRIPTION;ENCODING=QUOTED-PRINTABLE: " + bookingDao.getCourse().getCourseid() + " " + bookingDao.getCourse().getCoursename() + " Booking, \\n Booking Note: " + bookingDao.getNote() + "\n" +
//                            "UID:" + UUID.randomUUID() + "\n" +
//                            "SUMMARY: " + bookingDao.getCourse().getCourseid() + " " + bookingDao.getCourse().getCoursename() + " Booking " + "\n" +
//                            "DTSTAMP:" + formatDate(bookingDao.getBookingDate()) + "\n" +
//                            "DTSTART:" + getDateTime(bookingDao.getSlot().getStarttime().minusSeconds(10800), bookingDao.getBookingDate()) + "\n" +
//                            "DTEND:" + getDateTime(bookingDao.getSlot().getEndtime().minusSeconds(10800), bookingDao.getBookingDate()) + "\n" +
//                            "CLASS:PUBLIC\n" +
//                            "PRIORITY:5\n" +
//                            "TRANSP:OPAQUE\n" +
//                            "STATUS:CONFIRMED\n" +
//                            "LOCATION:B19 LLC\n" +
//                            "BEGIN:VALARM\n" +
//                            "DESCRIPTION:REMINDER\n" +
//                            "TRIGGER;RELATED=START:-PT15M\n" +
//                            "ACTION:DISPLAY\n" +
//                            "END:VALARM\n" +
//                            "END:VEVENT\n" +
//                            "END:VCALENDAR";
//
//
//            // creating the email body parts and adding the event invite
//            MimeBodyPart messageBodyPart = new MimeBodyPart();
//
//            messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
//            messageBodyPart.setHeader("Content-ID", "calendar_message");
//            messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(calendarContent, "text/calendar;method=REQUEST;name=\"invite.ics\"")));
//
//            MimeMultipart multipart = new MimeMultipart();
//            multipart.addBodyPart(messageBodyPart);
//
//            message.setContent(multipart);
//
//            // teams online meeting config
//            message.addHeader("X-MS-OLK-FORCEINSPECTOROPEN", "TRUE");
//            message.addHeader("X-MS-OLK-CONFTYPE", "3");
//            message.addHeader("X-MICROSOFT-DOES-BU", "TRUE");
//
//            // Send the email
//            mailSender.send(message);
//
//            System.out.println("Calendar invite sent successfully.");
//        }
//        catch (Exception ex)
//        {
//            System.out.println("error while sending email");
//            System.out.println(ex.getMessage());
//        }
//    }

    @Async
    public void sendInvite(BookingDao bookingDao)
    {
        // TODO - add link to body to allow people to view the booking seeda
        try
        {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + getToken());
            headers.set("Content-Type", "application/json");

            // main request body
            Map<String, Object> jsonBody = new HashMap<>();
            jsonBody.put("start",Map.of("dateTime", getDateTime(bookingDao.getSlot().getStarttime().plusSeconds(3600), bookingDao.getBookingDate()), "timeZone","Arabian Standard Time"));
            jsonBody.put("end",Map.of("dateTime", getDateTime(bookingDao.getSlot().getEndtime().plusSeconds(3600), bookingDao.getBookingDate()), "timeZone","Arabian Standard Time"));

            List<Object> attendees = new ArrayList<>();

            // add the leader to attendance
            attendees.add(Map.of("emailAddress",Map.of("address",bookingDao.getSlot().getLeader().getUserid() + "@student.polytechnic.bh"), "type","required"));

            // add the student to attendance
            attendees.add(Map.of("emailAddress",Map.of("address",bookingDao.getStudent().getUserid() + "@student.polytechnic.bh"), "type","required"));

            // add all members - if any (if group booking)
            if (bookingDao.getBookingMembers() != null && !bookingDao.getBookingMembers().isEmpty())
            {
                jsonBody.put("subject", bookingDao.getCourse().getCourseid() + " " + bookingDao.getCourse().getCoursename() + " Group Booking | " + formatDate(bookingDao.getBookingDate()) );
                jsonBody.put("body",Map.of("contentType","HTML", "content",
                        "<html>" +
                                "<body>" +
                                    "<h1>"+bookingDao.getCourse().getCourseid() + " " + bookingDao.getCourse().getCoursename() + " Group Booking | " + formatDate(bookingDao.getBookingDate())+"</h1>" +
                                    "<h3>Booking Notes:<h3>" +
                                    "<p>"+bookingDao.getNote()+"</p>" +
                                "</body>" +
                            "</html>"));

                for (BookingMemberDao student : bookingDao.getBookingMembers())
                {
                    attendees.add(Map.of("emailAddress",Map.of("address",student.getStudent().getUserid() + "@student.polytechnic.bh"), "type","required"));
                }
            }
            else
            {
                jsonBody.put("subject", bookingDao.getCourse().getCourseid() + " " + bookingDao.getCourse().getCoursename() + " Booking | " + formatDate(bookingDao.getBookingDate()) );
                jsonBody.put("body",Map.of("contentType","HTML", "content",
                        "<html>" +
                                "<body>" +
                                "<h1>"+bookingDao.getCourse().getCourseid() + " " + bookingDao.getCourse().getCoursename() + " Booking | " + formatDate(bookingDao.getBookingDate())+"</h1>" +
                                "<h3>Booking Notes:<h3>" +
                                "<p>"+bookingDao.getNote()+"</p>" +
                                "</body>" +
                                "</html>"));
            }

            // set to online location if online
            if (bookingDao.isIsonline())
            {
                jsonBody.put("location",Map.of("displayName", "Online"));
            }
            else
            {
                jsonBody.put("location",Map.of("displayName", "B19, LLC"));
            }


            jsonBody.put("attendees",attendees);
            jsonBody.put("isOnlineMeeting",true);
            jsonBody.put("onlineMeetingProvider","teamsForBusiness");
            jsonBody.put("allowNewTimeProposals", false);
            jsonBody.put("isReminderOn", true);
            jsonBody.put("responseRequested", false);
            jsonBody.put("reminderMinutesBeforeStart", 15);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(jsonBody, headers);
            // TODO - change the users id to env var
            ResponseEntity<String> response = restTemplate.exchange("https://graph.microsoft.com/v1.0/users/5a12e604-bded-4a55-98fb-60cc3df35f18/calendar/events", HttpMethod.POST, entity, String.class);

            HttpStatusCode statusCode = response.getStatusCode();
            System.out.println("Email Invite Response Status Code: " + statusCode);

        }
        catch (Exception ex)
        {
            System.out.println("error while sending email");
            System.out.println(ex.getMessage());
        }
    }

    // format the date of the booking
    private static String formatDate(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        return sdf.format(date);
    }

    // merge the date and the time from the booking and the slot time for email service
    private static String getDateTime(Instant time, Date date)
    {
        SimpleDateFormat dateF = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm:ss");

        return (dateF.format(date) +"T"+ timeF.format(Date.from(time)));
    }
}
