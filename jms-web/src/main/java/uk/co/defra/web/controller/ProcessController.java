package uk.co.defra.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import uk.co.defra.ejb.facade.JmsProcessFacade;
import uk.co.defra.job.exception.JobManagementApplicationException;
import uk.co.defra.job.exception.ProcessOperationException;
import uk.co.defra.job.model.JmsTaskSummary;
import uk.co.defra.web.exception.SpamException;

@Controller
public class ProcessController {

	Logger logger = LoggerFactory.getLogger(ProcessController.class.getName());

	private JmsProcessFacade jmsProcessFacade;

	@Autowired
	public ProcessController(JmsProcessFacade jmsProcessFacade) {
		this.jmsProcessFacade = jmsProcessFacade;
	}

	@RequestMapping(value = "/startProcess", method = RequestMethod.GET)
	public ModelAndView startProcessPage() {
		ModelAndView mav = new ModelAndView("startProcess");
		return mav;
	}

	@RequestMapping(value = "/process", method = RequestMethod.POST)
	public ModelAndView getArticles(@RequestParam("recipient") String recipient, Model model) {

		System.out.println("jmsProcessFacade is " + (jmsProcessFacade == null));

		long processInstanceId = -1;
		try {
			processInstanceId = jmsProcessFacade.startProcess(recipient);

		} catch (Exception e) {
			throw new JobManagementApplicationException(e);
		}

		model.addAttribute("message", "process instance (id = " + processInstanceId + ") has been started.");
		ModelAndView mav = new ModelAndView("index", "model", model);

		return mav;
	}

	@RequestMapping(value = "/task", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam("user") String user, @RequestParam("cmd") String cmd, @RequestParam(value = "taskId", required = false) Long taskId, Model model) throws Exception {
		ModelAndView mav = null;

		if (cmd.equals("list")) {

			List<JmsTaskSummary> taskList;
			taskList = jmsProcessFacade.retrieveTaskList(user);

			model.addAttribute("taskList", taskList);
			mav = new ModelAndView("task", "model", model);

		} else if (cmd.equals("approve")) {

			String message = "";
			try {
				jmsProcessFacade.approveTask(user, taskId);
				message = "Task (id = " + taskId + ") has been completed by " + user;
			} catch (ProcessOperationException e) {
				// Recoverable exception
				message = "Task operation failed. Please retry : " + e.getMessage();
			} catch (Exception e) {
				// Unexpected exception
				throw new JobManagementApplicationException(e);
			}

			model.addAttribute("message", message);
			mav = new ModelAndView("index", "model", model);

		}

		return mav;

	}

	@ExceptionHandler(SpamException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView handleSpamException(HttpServletRequest request, HttpServletResponse response, HttpSession session, SpamException e) {
		ModelMap model = new ModelMap();
		return new ModelAndView("/spam", model);
	}
}
