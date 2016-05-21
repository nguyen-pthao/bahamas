<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>TWC2 | Bahamas </title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <!-- jQuery library -->
        <script src="https://code.jquery.com/jquery-2.2.0.js"></script>
        <!-- Latest compiled JavaScript -->
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
        <!-- Ionicons -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
        <!-- Theme style -->
        <link rel="stylesheet" href="style/css/AdminLTE.min.css">
        <!-- AdminLTE Skins. Choose a skin from the css/skins
             folder instead of downloading all of them to reduce the load. -->
        <link rel="stylesheet" href="style/css/skins/_all-skins.min.css">
        <!-- daterange picker -->
        <link rel="stylesheet" href="plugins/daterangepicker/daterangepicker-bs3.css">
        <!-- date-range-picker -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.11.2/moment.min.js"></script>
        <script src="plugins/daterangepicker/daterangepicker.js"></script>

        <!-- SlimScroll -->
        <script src="plugins/slimScroll/jquery.slimscroll.min.js"></script>
        <!-- FastClick -->
        <script src="plugins/fastclick/fastclick.js"></script>
        <!-- AdminLTE App -->
        <script src="style/js/app.min.js"></script>
        <!-- AdminLTE for demo purposes -->
        <script src="style/js/demo.js"></script>

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>
    <!-- /.box -->
    <!-- general form elements disabled -->
    <div class="container" id="add-new-event">
        <div class="box-header with-border">
            <h3 class="box-title">Create Event</h3>
        </div>
        <!-- /.box-header -->
        <div class="box-body">
            <form role="form">
                <!-- select iterated events-->
                <div class="form-group">
                    <label>Iterated Events</label>
                    <select class="form-control">
                        <option>option 1</option>
                        <option>option 2</option>
                        <option>option 3</option>
                        <option>option 4</option>
                        <option>option 5</option>
                    </select>
                </div>
                <!-- text input -->
                <div class="form-group">
                    <label>Event Title</label>
                    <input type="text" class="form-control" placeholder="Enter ...">
                </div>
                <div class="form-group">


                    <label>Date - Start/End</label>
                    <div class="input-group">
                        <div class="input-group-addon">
                            <i class="fa fa-clock-o"></i>
                        </div>
                        <input type="text" class="form-control pull-right" id="reservationtime">
                    </div>
                    <!-- /.input group -->
                </div>

                <!-- Explain if other event -->
                <div class="form-group">
                    <label>Explain if others</label>
                    <textarea class="form-control" rows="3" placeholder="Enter ..."></textarea>
                </div>
                <!-- checkbox -->
                <div class="form-group">
                    <div class="checkbox">
                        <label>
                            <input type="checkbox">
                            Send Reminder
                        </label>
                    </div>
                </div>
                <!-- Event Location -->
                <div class="form-group">
                    <label>Select</label>
                    <select class="form-control">
                        <option>option 1</option>
                        <option>option 2</option>
                        <option>option 3</option>
                        <option>option 4</option>
                        <option>option 5</option>
                    </select>
                </div>
                <div align='right'>
                    <button type="submit" class="btn btn-primary">Create</button>
                </div>
        </div>
    </form>
</div>
<!-- /.box-body -->
<script>
    //Date range picker with time picker
    $('#reservationtime').daterangepicker({timePicker: true, timePickerIncrement: 30, format: 'MM/DD/YYYY h:mm A'});
</script>
</body>
</html>