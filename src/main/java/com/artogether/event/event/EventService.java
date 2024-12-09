package com.artogether.event.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class EventService {

	@Autowired
	private EventRepo eventRepo;

	public Event findById(int id) {
		return eventRepo.findById(id).orElse(null);
	}

	public Page<Event> findAllEventsAndPagination(String sortBy, int page, int size, String location, String catalog) {

		Comparator<Event> finalComparator = null;

		// 照報名人數排序
		Comparator<Event> enrollComparator = new Comparator<Event>() {
			public int compare(Event o1, Event o2) {
				return o1.getEnrolled() - o2.getEnrolled();
			}
		};

		// 照價格排序
		Comparator<Event> priceComparator = new Comparator<Event>() {
			public int compare(Event o1, Event o2) {
				return o1.getPrice() - o2.getPrice();
			}
		};

		// 照活動日期排序
		Comparator<Event> startDateComparator = new Comparator<Event>() {
			public int compare(Event o1, Event o2) {
				return o1.getStartDate().compareTo(o2.getStartDate());
			}
		};

		// 照活動id = 新舊排序(默認)
		Comparator<Event> defaultComparator = new Comparator<Event>() {
			public int compare(Event o1, Event o2) {
				return o2.getId() - o1.getId();
			}
		};

		switch (sortBy) {
		case "price":
		case "priceR":
			finalComparator = priceComparator;
			break;
		case "startDate":
		case "startDateR":
			finalComparator = startDateComparator;
			break;
		case "enrolled":
		case "enrolledR":
			finalComparator = enrollComparator;
			break;
		default:
			finalComparator = defaultComparator;
		}

		if (sortBy.contains("R")) {
			finalComparator = finalComparator.reversed();
		}

		List<Event> rawEventList = eventRepo.findAll();

		//活動地點篩選
		if (!"".equals(location)) {
			rawEventList = rawEventList.stream()
					.filter(e -> e.getLocation().contains(location))
					.toList();
		}

		//活動類型篩選
		if (!"".equals(catalog)) {
			rawEventList = rawEventList.stream().filter(e -> e.getCatalog().contains(catalog)).toList();
		}

		//判斷活動狀態 1->上架 2->延期、傳入comparator
		List<Event> sortedEventList = rawEventList.stream().filter(e -> (e.getStatus() == (byte) 1 || e.getStatus() == (byte) 2))
				.sorted(finalComparator).toList();

		int start = page * size;
		int end = Math.min(start + size, sortedEventList.size());
		List<Event> paginatedEventList = sortedEventList.subList(start, end);

		Pageable pageable = PageRequest.of(page,size);
		return new PageImpl<>(paginatedEventList, pageable, sortedEventList.size());
	}

	public List<Event> findAllEvents (String sortBy) {

		Comparator<Event> finalComparator = null;

		// 照報名人數排序
		Comparator<Event> enrollComparator = new Comparator<Event>() {
			public int compare(Event o1, Event o2) {
				return o1.getEnrolled() - o2.getEnrolled();
			}
		};

		// 照價格排序
		Comparator<Event> priceComparator = new Comparator<Event>() {
			public int compare(Event o1, Event o2) {
				return o1.getPrice() - o2.getPrice();
			}
		};

		// 照活動日期排序
		Comparator<Event> startDateComparator = new Comparator<Event>() {
			public int compare(Event o1, Event o2) {
				return o1.getStartDate().compareTo(o2.getStartDate());
			}
		};

		// 照活動id = 新舊排序(默認)
		Comparator<Event> defaultComparator = new Comparator<Event>() {
			public int compare(Event o1, Event o2) {
				return o2.getId() - o1.getId();
			}
		};

		switch (sortBy) {
			case "price":
			case "priceR":
				finalComparator = priceComparator;
				break;
			case "startDate":
			case "startDateR":
				finalComparator = startDateComparator;
				break;
			case "enrolled":
			case "enrolledR":
				finalComparator = enrollComparator;
				break;
			default:
				finalComparator = defaultComparator;
		}

		if (sortBy.contains("R")) {
			finalComparator = finalComparator.reversed();
		}

		//判斷活動狀態 1->上架 2->延期、傳入comparator
		List<Event> sortedEventList = eventRepo.findAll().stream().filter(e -> (e.getStatus() == (byte) 1 || e.getStatus() == (byte) 2))
				.sorted(finalComparator).toList();

		return sortedEventList;

	}

	public Event saveEvent(Event event) {
		return eventRepo.save(event);
	}

	public void statusUpdate(Event event) {
		Event e = findById(event.getId());
		e.setStatus(event.getStatus());
		eventRepo.save(e);
	}

	// 部分更新，不包括id、商家id、delayDate、status、enrolled的更新
	public void partialUpdate(Event partialE) {
		Event e = findById(partialE.getId());
		Event newE = Event.builder().id(partialE.getId())
						.businessMember(e.getBusinessMember())
						.name(partialE.getName())
						.location(partialE.getLocation())
						.startDate(partialE.getStartDate())
						.endDate(partialE.getEndDate())
						.delayDate(e.getDelayDate())
						.catalog(partialE.getCatalog())
						.price(partialE.getPrice())
						.description(partialE.getDescription())
						.status(e.getStatus())
						.maximum(partialE.getMaximum())
						.minimum(partialE.getMinimum())
						.enrolled(e.getEnrolled())
						.build();
		eventRepo.save(newE);
	}
	
	 public Page<Event> searchEvents(Map<String, String> searchCriteria, Pageable pageable) {
        Specification<Event> spec = EventSpecifications.dynamicQuery(searchCriteria);
        return eventRepo.findAll(spec, pageable);
    }

}
