package uet.japit.k62.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import uet.japit.k62.constant.MessageConstant;
import uet.japit.k62.constant.StatusCode;
import uet.japit.k62.dao.*;
import uet.japit.k62.exception.exception_define.detail.CategoryHasExistedException;
import uet.japit.k62.exception.exception_define.detail.CategoryNotFoundException;
import uet.japit.k62.exception.exception_define.detail.EventNotFoundException;
import uet.japit.k62.models.entity.*;
import uet.japit.k62.models.request.ReqCreateCategory;
import uet.japit.k62.models.request.ReqCreateEvent;
import uet.japit.k62.models.request.ReqCreateLocation;
import uet.japit.k62.models.request.ReqCreateTicketClass;
import uet.japit.k62.models.response.data_response.ResEvent;
import uet.japit.k62.models.response.http_response.HttpResponse;
import uet.japit.k62.models.response.http_response.MessageResponse;
import uet.japit.k62.service.authorize.AttributeTokenService;
import uet.japit.k62.util.StringConvert;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    @Autowired
    IUserDAO userDAO;

    @Autowired
    IEventDAO eventDAO;

    @Autowired
    ICategoryDAO categoryDAO;

    @Autowired
    ILocationDAO locationDAO;

    @Autowired
    DAO dao;

    public MessageResponse addEvent(HttpServletRequest httpRequest, ReqCreateEvent requestData) throws Exception {
        MessageResponse response = new MessageResponse();
        String token = httpRequest.getHeader("Authorization");
        String emailSendRequest = AttributeTokenService.getEmailFromToken(token);
        User userSendRequest = userDAO.findByEmail(emailSendRequest);
        Category category = categoryDAO.findById(requestData.getCategoryId()).get();
        if(category == null)
        {
            throw new CategoryNotFoundException();
        }
        Event newEvent = new Event(requestData);
        newEvent.setCategory(category);
        eventDAO.save(newEvent);



        //Create Location
        for(ReqCreateLocation reqCreateLocation : requestData.getLocationList())
        {
            Location locationEntity = new Location(reqCreateLocation);
            locationEntity.getEventList().add(newEvent);
            locationDAO.save(locationEntity);
            newEvent.getLocationList().add(locationEntity);
        }
        newEvent.setCreatedBy(userSendRequest.getEmail());
        eventDAO.save(newEvent);
        response.setStatusCode(StatusCode.OK);
        response.setMessage(MessageConstant.SUCCESS);
        return response;
    }

    public MessageResponse uploadImage(MultipartFile coverImage,
                                       MultipartFile mapImage,
                                       String eventId) throws Exception {

        Event event = eventDAO.findById(eventId).get();
        if(event == null)
        {
            throw new EventNotFoundException();
        }
        //upload CoverImage
        Path locationFile = Paths.get("resource/static/images");
        if(!Files.exists(locationFile))
        {
            Files.createDirectory(locationFile);
        }
        Files.copy(coverImage.getInputStream(), locationFile.resolve(System.currentTimeMillis() + coverImage.getOriginalFilename()));
        String filename = new String();
        Path file = locationFile.resolve(filename);
        UrlResource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            String coverLink = resource.getURL().toString();
            event.setCoverImageUrl(coverLink);
        }
        else throw new Exception("Could not read file");

        //upload mapEvent Image
        Files.copy(mapImage.getInputStream(), locationFile.resolve(System.currentTimeMillis() + mapImage.getOriginalFilename()));
        String mapImageName = new String();
        Path mapPath = locationFile.resolve(mapImageName);
        UrlResource resourceMapImage = new UrlResource(mapPath.toUri());
        if (resourceMapImage.exists() || resourceMapImage.isReadable()) {
            String mapLink = resource.getURL().toString();
            event.setMapImageUrl(mapLink);
        }
        else throw new Exception();
        eventDAO.save(event);
        return new MessageResponse(StatusCode.OK, MessageConstant.SUCCESS);
    }

    public HttpResponse getEvent()
    {
        return null;
    }

    public HttpResponse getEventById(String eventId) throws EventNotFoundException {
        Event event = eventDAO.findById(eventId).get();
        if(event == null)
        {
            throw new EventNotFoundException();
        }
        ResEvent resData = new ResEvent(event);
        return new HttpResponse(resData);
    }

    public HttpResponse search(String categoryId,
                               Date startTime,
                               Date endTime,
                               String location,
                               Boolean price)
    {
        HttpResponse response = new HttpResponse();
        List<Category> categoryList = categoryDAO.findAll();
        String query = "select * from event where ";
        if(categoryId != null)
        {
            for(Category category : categoryList)
            {
                if (category.getId().equals(categoryId))
                {
                    query += "event.category_id=" + "'"  +categoryId + "'";
                }
            }
        }
        System.out.println(query);
//        query += ";";
        List<Event> eventList = dao.search(query);
        response.setData(eventList);
        return response;
    }
}
