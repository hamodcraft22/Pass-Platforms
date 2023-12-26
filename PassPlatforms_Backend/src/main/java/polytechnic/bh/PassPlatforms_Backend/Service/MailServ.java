package polytechnic.bh.PassPlatforms_Backend.Service;

import jakarta.activation.DataHandler;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingMemberDao;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MailServ
{
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendInvite(BookingDao bookingDao)
    {
        try
        {
            // creating the initial message
            MimeMessage message = mailSender.createMimeMessage();

            // headers
            message.addHeaderLine("method=REQUEST");
            message.addHeaderLine("charset=UTF-8");
            message.addHeaderLine("component=VEVENT");
            message.setFrom(new InternetAddress("mohammed.adel.j22@hotmail.com"));

            // send it to the leader
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("202002789@student.polytechnic.bh"));

            // send it to the main student
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(bookingDao.getStudent().getUserid() + "@passplatfromsauthtest.onmicrosoft.com"));

            // list of student email - if group
            List<String> studentMembers = new ArrayList<>();

            // if it is a group, send to them as well
            if (bookingDao.getBookingMembers() != null && !bookingDao.getBookingMembers().isEmpty())
            {
                // header info
                message.setSubject(bookingDao.getCourse().getCourseid() + " " + bookingDao.getCourse().getCoursename() + " - Group Booking");

                for (BookingMemberDao student : bookingDao.getBookingMembers())
                {
                    studentMembers.add(student.getStudent().getUserid() + "@passplatfromsauthtest.onmicrosoft.com");
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(student.getStudent().getUserid() + "@passplatfromsauthtest.onmicrosoft.com"));
                }
            }
            else
            {
                message.setSubject(bookingDao.getCourse().getCourseid() + " " + bookingDao.getCourse().getCoursename());
            }


            // calendar invite file
            String calendarContent =
                    "BEGIN:VCALENDAR\n" +
                            "METHOD:REQUEST\n" +
                            "PRODID:Microsoft Exchange Server 2010\n" +
                            "VERSION:2.0\n" +
                            "BEGIN:VTIMEZONE\n" +
                            "TZID:Asia/Bahrain\n" +
                            "END:VTIMEZONE\n" +
                            "BEGIN:VEVENT\n" +
                            "ORGANIZER;EMAIL=202002789@student.polytechnic.bh;ROLE=CHAIR:mailto:202002789@student.polytechnic.bh\n" +
                            "ATTENDEE;" + bookingDao.getStudent().getUserName() + ";EMAIL=" + (bookingDao.getStudent().getUserid() + "@passplatfromsauthtest.onmicrosoft.com") + ";ROLE=REQ-PARTICIPANT:mailto:" + (bookingDao.getStudent().getUserid() + "@passplatfromsauthtest.onmicrosoft.com") + "\n";

            if (!studentMembers.isEmpty())
            {
                for (String studentEmail : studentMembers)
                {
                    calendarContent += ("ATTENDEE;EMAIL=" + studentEmail + ";ROLE=REQ-PARTICIPANT:mailto:" + studentEmail + "\n");
                }
            }

            calendarContent +=
                    "DESCRIPTION;ENCODING=QUOTED-PRINTABLE: " + bookingDao.getCourse().getCourseid() + " " + bookingDao.getCourse().getCoursename() + " Booking, \\n Booking Note: " + bookingDao.getNote() + "\n" +
                            "UID:" + UUID.randomUUID() + "\n" +
                            "SUMMARY: " + bookingDao.getCourse().getCourseid() + " " + bookingDao.getCourse().getCoursename() + " Booking " + "\n" +
                            "DTSTAMP:" + formatDate(bookingDao.getBookingDate()) + "\n" +
                            "DTSTART:" + getDateTime(bookingDao.getSlot().getStarttime().minusSeconds(10800), bookingDao.getBookingDate()) + "\n" +
                            "DTEND:" + getDateTime(bookingDao.getSlot().getEndtime().minusSeconds(10800), bookingDao.getBookingDate()) + "\n" +
                            "CLASS:PUBLIC\n" +
                            "PRIORITY:5\n" +
                            "TRANSP:OPAQUE\n" +
                            "STATUS:CONFIRMED\n" +
                            "LOCATION:B19 LLC\n" +
                            "BEGIN:VALARM\n" +
                            "DESCRIPTION:REMINDER\n" +
                            "TRIGGER;RELATED=START:-PT15M\n" +
                            "ACTION:DISPLAY\n" +
                            "END:VALARM\n" +
                            "END:VEVENT\n" +
                            "END:VCALENDAR";


            System.out.println(calendarContent);

            // creating the email body parts and adding the event invite
            MimeBodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
            messageBodyPart.setHeader("Content-ID", "calendar_message");
            messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(calendarContent, "text/calendar;method=REQUEST;name=\"invite.ics\"")));

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            // teams online meeting config
            message.addHeader("X-MS-OLK-FORCEINSPECTOROPEN", "TRUE");
            message.addHeader("X-MS-OLK-CONFTYPE", "3");
            message.addHeader("X-MICROSOFT-DOES-BU", "TRUE");

            // Send the email
            mailSender.send(message);

            System.out.println("Calendar invite sent successfully.");
        }
        catch (Exception ex)
        {
            System.out.println("error while sending email");
            System.out.println(ex.getMessage());
        }
    }

    private static String formatDate(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");

        return sdf.format(date);
    }

    private static String getDateTime(Instant time, Date date)
    {
        SimpleDateFormat dateF = new SimpleDateFormat("yyyyMMdd'T'");
        SimpleDateFormat timeF = new SimpleDateFormat("HHmmss'Z'");

        return (dateF.format(date) + timeF.format(Date.from(time)));
    }
}
