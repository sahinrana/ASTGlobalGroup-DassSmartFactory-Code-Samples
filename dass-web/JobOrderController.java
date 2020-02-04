package com.dasssmart.controller;

import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.dasssmart.dto.*;
import com.dasssmart.repository.JobOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dasssmart.core.RestListMessage;
import com.dasssmart.service.JobOrderService;
import com.dasssmart.service.ProductTreeDetailService;

@RestController
@RequestMapping(value = "/jobOrder")

public class JobOrderController {

	@Autowired
	private JobOrderService jobOrderService;

	@Autowired
	private JobOrderRepository jobOrderRepository;

	@Autowired
	private ProductTreeDetailService productTreeDetailService;

	@PostMapping(value = "/create/manuelJobOrder")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLEEDITCREATEJOBORDER')")
	public ResponseEntity<Void> manuelJobOrder(@RequestBody @Valid CreateJobOrderRequest requestDto) {

		List<CreateJobOrderRequest> list = new ArrayList<>();
		list.add(requestDto);
		jobOrderService.createManuelJobOrder(list);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@PostMapping(value = "/create/autoJobOrder")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLEEDITCREATEJOBORDER')")
	public ResponseEntity<Void> autoJobOrder(@RequestBody @Valid List<CreateJobOrderRequest> requestDto) {
		jobOrderService.createJobOrder(requestDto);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@PostMapping(value = "/filterJobOrderByStatusReady")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLESHOWCREATEJOBORDER')")
	public ResponseEntity<RestListMessage<ResponseJobOrdersByStatusReadyFilterDto>> filterJobOrderByStatusReady(@RequestBody @Valid RequestJobOrdersByStatusReadyFilterDto filter) {
		return new ResponseEntity<RestListMessage<ResponseJobOrdersByStatusReadyFilterDto>>(jobOrderService.getJobOrdersByStatusReadyFilter(filter), HttpStatus.OK);
	}

	@PostMapping(value = "/filterJobOrder")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLESHOWCREATEJOBORDER')")
	public ResponseEntity<RestListMessage<ResponseJobOrderFilterDto>> filterJobOrder(@RequestBody @Valid RequestJobOrderFilterDto filter) {
		return new ResponseEntity<RestListMessage<ResponseJobOrderFilterDto>>(jobOrderService.filterJobOrder(filter), HttpStatus.OK);
	}

	@PostMapping(value = "/filterJobOrderByStatusPlannedOrProcessing")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLESHOWCREATEJOBORDER')")
	public ResponseEntity<RestListMessage<ResponseJobOrderByStatusPlannedAndProcessingFilterDto>> filterJobOrderByStatusPlannedOrProcessing(
			@RequestBody @Valid RequestJobOrderByStatusPlannedAndProcessingFilterDto filter) {
		return new ResponseEntity<RestListMessage<ResponseJobOrderByStatusPlannedAndProcessingFilterDto>>(jobOrderService.getJobOrderByStatusPlannedAndProcessingFilter(filter),
				HttpStatus.OK);
	}

	@PostMapping(value = "/updateJobOrderStatusReadyToPlan")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLEEDITCREATEJOBORDER')")
	public ResponseEntity<Void> updateJobOrderStatusReadyToPlan(@RequestBody @Valid RequestPlannedJobOrderDto requestDto) {
		jobOrderService.getPlannedJobOrder(requestDto);
		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	}

	@PostMapping(value = "/divideJobOrder")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLEEDITCREATEJOBORDER')")
	public ResponseEntity<Void> divideJobOrder(@RequestBody @Valid RequestDivisionJobOrderDto requestDto) {
		jobOrderService.updateAndDeletedJobOrder(requestDto);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@PostMapping(value = "/joinJobOrders")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLEEDITCREATEJOBORDER')")
	public ResponseEntity<Void> joinJobOrder(@RequestBody @Valid RequestJoinJobOrderDto requestDto) {
		jobOrderService.joinJobOrders(requestDto);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@PostMapping(value = "/mergeJobOrder")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLEEDITCREATEJOBORDER')")
	public ResponseEntity<Void> divideJobOrder(@RequestBody @Valid List<RequestMergeJobOrders> requestDto) {
		jobOrderService.mergeJobOrders(requestDto);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@GetMapping(value = "/createJobOrderRequestByOrderDetailId/{orderDetailId}/{expectedQuantity}")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLEEDITCREATEJOBORDER')")
	public ResponseEntity<List<CreateJobOrderRequest>> createJobOrderRequestByOrderDetailId(@PathVariable int orderDetailId, @PathVariable double expectedQuantity) {

		return new ResponseEntity<>(productTreeDetailService.createJobOrderRequest(orderDetailId, expectedQuantity), HttpStatus.OK);
	}

	@PostMapping(value = "/updateGant")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLEEDITCREATEJOBORDER')")
	public ResponseEntity<Void> updateJobOrderForGant(@RequestBody @Valid GanttDto requestDto) throws ParseException {
		jobOrderService.updateGant(requestDto);
		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	}

	@GetMapping(value = "/changeJobOrderStatusToReady/{Id}")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLEEDITCREATEJOBORDER')")
	public ResponseEntity<Void> changeStatusReady(@PathVariable int Id) throws ParseException {
		jobOrderService.changeStatusJobOrderCancelledToReady(Id);
		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	}

	@GetMapping(value = "/changeJobOrderStatusToCancelled/{Id}")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLEEDITCREATEJOBORDER')")
	public ResponseEntity<Void> changeStatusCancelled(@PathVariable int Id) throws ParseException {
		jobOrderService.changeStatusJobOrderCancelled(Id);
		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	}

	@PostMapping(value = "/update/Schedule")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLEEDITCREATEJOBORDER')")
	public ResponseEntity<Void> updateForSchedule(@RequestBody @Valid List<RequestJobOrderScheduleDto> requestDto) {
		jobOrderService.updatejobOrderAccordingToSchedule(requestDto);
		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	}

	@GetMapping(value = "/actualStart/{id}")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLEEDITCREATEJOBORDER')")
	public ResponseEntity<Time> getTime(@PathVariable int id) throws ParseException {

		return new ResponseEntity<Time>(jobOrderRepository.getTime(id),HttpStatus.ACCEPTED);
	}

	@GetMapping(value = "/dailyReport")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLEEDITCREATEJOBORDER')")
	public ResponseEntity<List<DailyJobOrderDto>> getDailyJobOrderReport() throws ParseException {

		return new ResponseEntity<List<DailyJobOrderDto>>(jobOrderService.getDailyJobOrderReport(),HttpStatus.ACCEPTED);
	}

	@PostMapping(value = "/filterPowerAnalise")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLESHOWCREATEJOBORDER')")
	public ResponseEntity<RestListMessage<ResponsePowerCostJobOrderDto>> filterPowerAnaliseJobOrder(
			@RequestBody @Valid RequestPowerJobOrderDto filter) {
		return new ResponseEntity<RestListMessage<ResponsePowerCostJobOrderDto>>(jobOrderService.getPowerCostJobOrderByDate(filter),
				HttpStatus.OK);
	}
}
