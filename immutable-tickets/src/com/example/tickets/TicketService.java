package com.example.tickets;

import java.util.ArrayList;
import java.util.List;

/**
 * - Uses the Builder to create valid, immutable tickets.
 * - "Updates" now return a new instance instead of mutating the existing one.
 * - Validation is deferred entirely to the Builder.
 */
public class TicketService {

    public IncidentTicket createTicket(String id, String reporterEmail, String title) {
        //We removed all the scattered validation here!
        // We trust the Builder's .build() method to enforce all the rules centrally.

        List<String> tags = new ArrayList<>();
        tags.add("NEW");

        //We build the ticket in one atomic step. It is fully valid the second it is born.
        return new IncidentTicket.Builder(id, reporterEmail, title)
                .priority("MEDIUM")
                .source("CLI")
                .customerVisible(false)
                .tags(tags)
                .build();
    }

    //Changed return type from 'void' to 'IncidentTicket'
    public IncidentTicket escalateToCritical(IncidentTicket t) {
        //We don't mutate the old ticket. We use toBuilder() to copy its
        // existing data, apply our new changes, and stamp out a brand new ticket.
        return t.toBuilder()
                .priority("CRITICAL")
                .addTag("ESCALATED") // Safely adds to a new copy of the list
                .build();
    }

    //Changed return type from 'void' to 'IncidentTicket'
    public IncidentTicket assign(IncidentTicket t, String assigneeEmail) {
        //Removed scattered validation. The Builder will catch bad emails.
        return t.toBuilder()
                .assigneeEmail(assigneeEmail)
                .build();
    }
}