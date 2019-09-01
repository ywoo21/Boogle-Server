package kr.ant.booksharing.controller;

import kr.ant.booksharing.service.ListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DeviceRedirectionController {

    public DeviceRedirectionController() {}


    @RequestMapping("/detect")
    public ResponseEntity home(final Device device) {
        String msg = "";
        if (device.isMobile()) {
            msg = "mobile";
        } else if (device.isTablet()) {
            msg = "tablet";
        } else {
            msg = "desktop";
        }
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
