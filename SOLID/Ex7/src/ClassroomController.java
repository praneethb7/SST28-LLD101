public class ClassroomController {

    private final DeviceRegistry reg;

    public ClassroomController(DeviceRegistry reg) {
        this.reg = reg;
    }

    public void startClass() {

        PowerControl pjPower = reg.getFirst(PowerControl.class);
        pjPower.powerOn();

        InputConnectable pjInput =
                reg.getFirst(InputConnectable.class);
        pjInput.connectInput("HDMI-1");

        BrightnessControl lights =
                reg.getFirst(BrightnessControl.class);
        lights.setBrightness(60);

        TemperatureControl ac =
                reg.getFirst(TemperatureControl.class);
        ac.setTemperatureC(24);

        AttendanceScanning scan =
                reg.getFirst(AttendanceScanning.class);

        System.out.println(
                "Attendance scanned: present=" +
                        scan.scanAttendance()
        );
    }

    public void endClass() {
        System.out.println("Shutdown sequence:");
        for (PowerControl p : reg.getAll(PowerControl.class)) {
            p.powerOff();
        }
    }
}