
package org.jbpm.defra.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.kie.api.task.model.TaskSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import uk.co.defra.job.ejb.exception.ProcessOperationException;
import uk.co.defra.job.ejb.exception.SpamException;
import uk.co.defra.job.mngnt.ejb.inter.ProcessLocal;
import uk.co.defra.job.mngnt.ejb.inter.TaskLocal;
import uk.co.defra.job.mngnt.facade.EjbServiceFacade;
//import uk.co.defra.job.mngnt.ejb.Process;

@Controller
public class ProcessController {

	Logger logger = LoggerFactory.getLogger(ProcessController.class.getName());

	 String PROCESS_BEAN_NAME= "java:module/ProcessBean!uk.co.defra.job.mngnt.ejb.ProcessLocal";//"ejb:digitiser-web/ProcessBean!uk.co.defra.job.mngnt.ejb.ProcessRemote";
	 String TASK_BEAN_NAME ="java:module/TaskBean";//"ejb:digitiser-web/TaskBean!uk.co.defra.job.mngnt.ejb.TaskRemote";
	  
  @EJB(mappedName="java:module/EjbServiceFacadeImpl")  	
  private EjbServiceFacade ejbServiceFacade;	
	
//  @EJB(mappedName="ejb:digitiser-web/ProcessBean!uk.co.defra.job.mngnt.ejb.ProcessRemote";
  private ProcessLocal processBean;

  
 // @EJB(mappedName="ejb:global/digitiser-web/TaskBean!uk.co.defra.job.mngnt.ejb.TaskRemote";
  private TaskLocal taskBean;


@RequestMapping(value = "/startProcess", method= RequestMethod.GET)
public ModelAndView startProcessPage() {
    ModelAndView mav = new ModelAndView("startProcess");
    return mav;
}
  
    /**
     * Controller Method to list all articles.
     *
     * @return the ModelAndView.
     */
    @RequestMapping(value = "/process", method= RequestMethod.POST)
    public ModelAndView getArticles(@RequestParam("recipient") String recipient,Model model) {
    	 
    	
    	
    	 long processInstanceId = -1;
         try {
        	 
        	 processBean = (ProcessLocal)ejbServiceFacade.getServiceBean(PROCESS_BEAN_NAME);
        	 if(processBean == null) {
        		 logger.info("&&&&&&&&&&&&&&&&&&&&&   Process Bean is null");
        	 }
             processInstanceId = processBean.startProcess(recipient);
             
         } catch (Exception e) {
             throw new RuntimeException(e);
         }

        model.addAttribute("message", "process instance (id = "
                + processInstanceId + ") has been started.");
        ModelAndView mav = new ModelAndView("index","model",model);
        
        return mav;
    }

    /**
	 * Controller Method to show one article.
	 *
	 * @param id the id value of the article.
	 * @return the ModelAndView.
	 * @throws Exception 
	 */
	@RequestMapping(value="/task", method=RequestMethod.GET)
	public ModelAndView getView(@RequestParam("user") String user,@RequestParam("cmd") String cmd,@RequestParam(value= "taskId", required=false) Long taskId,Model model) throws Exception {
		   ModelAndView mav = null;
		try {    
		taskBean =  (TaskLocal)ejbServiceFacade.getServiceBean(TASK_BEAN_NAME);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		if (cmd.equals("list")) {
	
	        List<TaskSummary> taskList;
	        taskList = taskBean.retrieveTaskList(user);
	
	        model.addAttribute("taskList", taskList);
	        mav = new ModelAndView("task","model",model);
	        
	    } else if (cmd.equals("approve")) {
	
	        String message = "";
	        try {
	        	taskBean.approveTask(user, taskId);
	            message = "Task (id = " + taskId + ") has been completed by " + user;
	        } catch (ProcessOperationException e) {
	            // Recoverable exception
	            message = "Task operation failed. Please retry : " + e.getMessage();
	        } catch (Exception e) {
	            // Unexpected exception
	            throw new ServletException(e);
	        }
	        
	        model.addAttribute("message", message);
	      mav = new ModelAndView("index","model",model);
	      
	    }
	
		return mav;
	
	}

    /**
     * Handles SpamExceptions.
     *
     * @param request the request.
     * @param response the response.
     * @param session the session.
     * @param e the SpamException.
     * @return the error page.
     */
    @ExceptionHandler(SpamException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleSpamException(HttpServletRequest request, HttpServletResponse response,
                                            HttpSession session, SpamException e) {
        ModelMap model = new ModelMap();
        return new ModelAndView("/spam", model);
    }
}
