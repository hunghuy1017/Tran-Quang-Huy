<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Swim Calendar</title>
        <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
        <link href="css/Dashboard.css" rel="stylesheet">
        <style>
            .day-of-week {
                width: 14.26%; /* 100% / 7 ngày */
                text-align: center;
            }
        </style>
    </head>
    <body>
        <%@include file="DashBoardUser.jsp" %>
        <div class="container mx-auto px-4 py-10">
            <!-- Message -->
            <c:if test="${not empty mess}">
                <p class="text-red-500 mb-4">${mess}</p>
            </c:if>

            <!-- Swim Package Info -->
            <div class="mb-4 p-4 bg-gray-100 rounded-lg">
                <c:choose>
                    <c:when test="${swimPackage.remainingDays > 0}">
                        <p class="text-lg font-semibold">Package: ${swimPackage.packageName}</p>
                        <p>Swim Days Remaining: ${swimPackage.remainingDays}</p>
                        <p>Expiry Date: ${swimPackage.expiryDate}</p>
                        <c:if test="${swimPackage.isExpiringSoon}">
                            <p class="text-red-500 font-bold">Your package is expiring soon!</p>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <p class="text-gray-500">No active swim package.</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Calendar Navigation -->
            <div class="bg-white rounded-lg shadow overflow-hidden">
                <div class="flex items-center justify-between py-2 px-6">
                    <div>
                        <span class="text-lg font-bold text-gray-800">
                            <fmt:formatDate value="${java.util.Date.from(java.time.LocalDate.of(year, month + 1, 1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant())}" pattern="MMMM" />
                        </span>
                        <span class="ml-1 text-lg text-gray-600 font-normal">${month + 1} / ${year}</span>
                    </div>
                    <div class="border rounded-lg px-1">
                        <a href="CalendarServlet?service=showCalendar&month=${month - 1}&year=${year}" class="inline-flex items-center p-1 ${month == 0 ? 'opacity-25 cursor-not-allowed' : 'hover:bg-gray-200'}">
                            <svg class="h-6 w-6 text-gray-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
                            </svg>
                        </a>
                        <div class="border-r inline-flex h-6"></div>
                        <a href="CalendarServlet?service=showCalendar&month=${month + 1}&year=${year}" class="inline-flex items-center p-1 ${month == 11 ? 'opacity-25 cursor-not-allowed' : 'hover:bg-gray-200'}">
                            <svg class="h-6 w-6 text-gray-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
                            </svg>
                        </a>
                    </div>
                </div>

                <!-- Calendar Header (Days of Week) -->
                <div class="flex flex-wrap border-b">
                    <c:forEach items="${daysOfWeek}" var="day">
                        <div class="day-of-week px-2 py-2">
                            <div class="text-gray-600 text-sm uppercase tracking-wide font-bold text-center">${day}</div>
                        </div>
                    </c:forEach>
                </div>

                <!-- Calendar Body -->
                <!-- Calendar Body -->
                <div class="flex flex-wrap border-t border-l">
                    <!-- Hiển thị các ngày trống ở đầu tháng -->
                    <c:forEach items="${blankDays}" var="blank">
                        <div class="calendar-day text-center border-r border-b px-4 pt-2">
                            <div class="inline-flex w-6 h-6 items-center justify-center text-center leading-none"></div>
                        </div>
                    </c:forEach>
                    <!-- Hiển thị các ngày trong tháng -->
                    <c:forEach items="${days}" var="currentDay">
                        <div class="calendar-day text-center border-r border-b px-4 pt-2 relative" style="width: 14.26%; min-height: 120px; max-height: 120px;">
                            <div class="inline-flex w-6 h-6 items-center justify-center text-center leading-none rounded-full transition ease-in-out duration-100
                                 ${currentDay == java.time.LocalDate.now().dayOfMonth && month == java.time.LocalDate.now().monthValue - 1 && year == java.time.LocalDate.now().year ? 'bg-blue-500 text-white' : 'text-gray-700 hover:bg-blue-200'}">
                                ${currentDay}
                            </div>
                            <div class="h-20 overflow-y-auto mt-1">
                                <c:forEach items="${events}" var="event">
                                    <!-- Kiểm tra và gán giá trị rõ ràng -->
                                    <c:set var="eventDateStr" value="${not empty event.eventDate ? event.eventDate : 'null'}" />
                                    <c:set var="calendarDateStr" value="${year}-${month + 1 < 10 ? '0' : ''}${month + 1}-${currentDay < 10 ? '0' : ''}${currentDay}" />
                                    <c:if test="${not empty calendarDateStr and eventDateStr == calendarDateStr}">
                                        <div class="event px-2 py-1 rounded-lg mt-1 overflow-hidden border
                                             ${event.eventTheme == 'blue' ? 'border-blue-200 text-blue-800 bg-blue-100' : 
                                               event.eventTheme == 'red' ? 'border-red-200 text-red-800 bg-red-100' : 
                                               'border-green-200 text-green-800 bg-green-100'}">
                                            <p class="truncate">${event.eventTitle}</p>
                                            <c:if test="${event.eventType == 'lesson'}">
                                                <a href="#changeLessonModal" onclick="openChangeLessonModal(${event.eventId}, '${event.eventDate}')" class="text-blue-500 text-xs">Change</a>
                                                <a href="#rateTrainerModal" onclick="openRateTrainerModal(${event.eventId})" class="text-green-500 text-xs ml-2">Rate</a>
                                            </c:if>
                                            <c:if test="${event.eventType == 'party'}">
                                                <a href="#viewEventModal" onclick="openViewEventModal(${event.eventId})" class="text-red-500 text-xs">View</a>
                                            </c:if>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- Change Lesson Modal -->
            <div id="changeLessonModal" class="modal">
                <div class="modal-content">
                    <h2 class="font-bold text-2xl mb-6 text-gray-800 border-b pb-2">Request Change Lesson</h2>
                    <form action="CalendarServlet" method="post">
                        <input type="hidden" name="service" value="requestChangeLesson">
                        <input type="hidden" name="booking_id" id="changeLessonBookingId">
                        <div class="mb-4">
                            <label class="text-gray-800 block mb-1 font-bold text-sm tracking-wide">New Date</label>
                            <input class="bg-gray-200 border-2 border-gray-200 rounded-lg w-full py-2 px-4 text-gray-700" type="date" name="new_date" required>
                        </div>
                        <div class="mt-8 text-right">
                            <button type="button" onclick="closeModal('changeLessonModal')" class="bg-white hover:bg-gray-100 text-gray-700 font-semibold py-2 px-4 border border-gray-300 rounded-lg shadow-sm mr-2">Cancel</button>
                            <button type="submit" class="bg-gray-800 hover:bg-gray-700 text-white font-semibold py-2 px-4 border border-gray-700 rounded-lg shadow-sm">Submit</button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Rate Trainer Modal -->
            <div id="rateTrainerModal" class="modal">
                <div class="modal-content">
                    <h2 class="font-bold text-2xl mb-6 text-gray-800 border-b pb-2">Rate Trainer</h2>
                    <form action="CalendarServlet" method="post">
                        <input type="hidden" name="service" value="rateTrainer">
                        <input type="hidden" name="booking_id" id="rateTrainerBookingId">
                        <div class="mb-4">
                            <label class="text-gray-800 block mb-1 font-bold text-sm tracking-wide">Rating (1-5)</label>
                            <input class="bg-gray-200 border-2 border-gray-200 rounded-lg w-full py-2 px-4 text-gray-700" type="number" name="rating" min="1" max="5" required>
                        </div>
                        <div class="mb-4">
                            <label class="text-gray-800 block mb-1 font-bold text-sm tracking-wide">Comment</label>
                            <textarea class="bg-gray-200 border-2 border-gray-200 rounded-lg w-full py-2 px-4 text-gray-700" name="comment"></textarea>
                        </div>
                        <div class="mt-8 text-right">
                            <button type="button" onclick="closeModal('rateTrainerModal')" class="bg-white hover:bg-gray-100 text-gray-700 font-semibold py-2 px-4 border border-gray-300 rounded-lg shadow-sm mr-2">Cancel</button>
                            <button type="submit" class="bg-gray-800 hover:bg-gray-700 text-white font-semibold py-2 px-4 border border-gray-700 rounded-lg shadow-sm">Submit</button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- View Event Modal -->
            <div id="viewEventModal" class="modal">
                <div class="modal-content">
                    <h2 class="font-bold text-2xl mb-6 text-gray-800 border-b pb-2">View Event</h2>
                    <p>Event ID: <span id="eventId"></span></p>
                    <div class="mt-8 text-right">
                        <button type="button" onclick="closeModal('viewEventModal')" class="bg-white hover:bg-gray-100 text-gray-700 font-semibold py-2 px-4 border border-gray-300 rounded-lg shadow-sm mr-2">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <script>
            function openModal(modalId) {
                document.getElementById(modalId).classList.add('show');
            }
            function closeModal(modalId) {
                document.getElementById(modalId).classList.remove('show');
            }
            function openChangeLessonModal(bookingId, date) {
                document.getElementById('changeLessonBookingId').value = bookingId;
                document.getElementById('changeLessonModal').classList.add('show');
            }
            function openRateTrainerModal(bookingId) {
                document.getElementById('rateTrainerBookingId').value = bookingId;
                document.getElementById('rateTrainerModal').classList.add('show');
            }
            function openViewEventModal(eventId) {
                document.getElementById('eventId').innerText = eventId;
                document.getElementById('viewEventModal').classList.add('show');
            }
        </script>
    </body>
</html>