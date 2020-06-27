package com.java.relay42.web.rest;

import com.java.relay42.entity.Sensor;
import com.java.relay42.exception.BadRequestAlertException;
import com.java.relay42.repository.DeviceRepository;
import com.java.relay42.util.HeaderUtil;
import com.java.relay42.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing {@link Sensor}.
 */
@RestController
@RequestMapping("/api")
public class DeviceResource {

    private static final String ENTITY_NAME = "device";

    private final Logger log = LoggerFactory.getLogger(DeviceResource.class);

    private final DeviceRepository deviceRepository;

    @Value("${spring.application.name}")
    private String applicationName;

    public DeviceResource(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    /**
     * {@code POST  /devices} : Create a new device.
     *
     * @param sensor the device to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new device, or with status {@code 400 (Bad Request)} if the device has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/devices")
    public ResponseEntity<Sensor> createDevice(@RequestBody Sensor sensor) throws URISyntaxException {
        log.debug("REST request to save Device : {}", sensor);
        if (sensor.getId() != null) {
            throw new BadRequestAlertException("A new device cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sensor.setId(UUID.randomUUID());
        Sensor result = deviceRepository.save(sensor);
        return ResponseEntity.created(new URI("/api/devices/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /devices} : Updates an existing device.
     *
     * @param sensor the device to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated device,
     * or with status {@code 400 (Bad Request)} if the device is not valid,
     * or with status {@code 500 (Internal Server Error)} if the device couldn't be updated.
     */
    @PutMapping("/devices")
    public ResponseEntity<Sensor> updateDevice(@RequestBody Sensor sensor) {
        log.debug("REST request to update Device : {}", sensor);
        if (sensor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Sensor result = deviceRepository.save(sensor);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sensor.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /devices} : get all the devices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of devices in body.
     */
    @GetMapping("/devices")
    public List<Sensor> getAllDevices() {
        log.debug("REST request to get all Devices");
        return deviceRepository.findAll();
    }

    /**
     * {@code GET  /devices/:id} : get the "id" device.
     *
     * @param id the id of the device to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the device, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/devices/{id}")
    public ResponseEntity<Sensor> getDevice(@PathVariable UUID id) {
        log.debug("REST request to get Device : {}", id);
        Optional<Sensor> device = deviceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(device);
    }

    /**
     * {@code DELETE  /devices/:id} : delete the "id" device.
     *
     * @param id the id of the device to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/devices/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID id) {
        log.debug("REST request to delete Device : {}", id);
        deviceRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
