<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>User Dashboard</title>
        <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Dashboard.css">
        <script defer src="https://unpkg.com/alpinejs@3.x.x/dist/cdn.min.js"></script>
    </head>
    <body class="antialiased sans-serif bg-gray-100 ml-64">
        <aside id="left-panel" class="left-panel">
            <nav class="navbar navbar-default">
                <div class="navbar-header">
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#main-menu" aria-controls="main-menu" aria-expanded="false" aria-label="Toggle navigation">
                        
                        <span class="brand-title">Option</span>
                        <span class="brand-circle"></span>
                        <i class="fas fa-bars"></i>
                    </button>

                </div>
                <div id="main-menu" class="main-menu collapse navbar-collapse">
                    <ul class="nav navbar-nav">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/UserProfile> <i class="menu-icon fas fa-tachometer-alt"></i>Dashboard </a>
                        </li>
                        <li class="nav-item menu-item-has-children dropdown">
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/CalendarServlet?service=showCalendar" > <i class="menu-icon fa fa-th"></i>Calendar</a>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/home" > <i class="menu-icon fa fa-th"></i>Home</a>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/UserProfile" > <i class="menu-icon fa fa-th"></i>Use Profile</a>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/userHistoryPayment" > <i class="menu-icon fa fa-th"></i>History Payment ${count}</a>
                            <a class="dropdown-item" href="#" > <i class="menu-icon fa fa-th"></i>Event Registered</a>
                            <a class="dropdown-item" href="#" > <i class="menu-icon fa fa-th"></i>Support</a>

                        </li>
                    </ul>
                </div><!-- /.navbar-collapse -->
            </nav>
        </aside>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
