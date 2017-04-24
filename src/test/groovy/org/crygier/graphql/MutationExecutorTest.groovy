package org.crygier.graphql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification;

@Configuration
@ContextConfiguration(loader = SpringApplicationContextLoader, classes = TestApplication)
class MutationExecutorTest extends Specification {

    @Autowired
    private GraphQLExecutor executor

    def 'Insert new user mutation'() {
        given:
        def query = '''
        mutation QueryType_JPA {
            createUser(id: 1, age: 22, cashAmount: 1000.10, cashAvailable: 50.50, cashInBank: 100.6, stepsDoneOverall: 100000000000000000, stepsDoneToday: 10000, name: "Max", bornOn: "1994-06-26T12:30:15", lastOnLineOn: "2017-04-15") {
                id
                age
                cashAmount
                cashAvailable
                cashInBank
                stepsDoneOverall
                stepsDoneToday
                name
                bornOn
                lastOnLineOn
              }
        }
        '''
        def expected = [
            "createUser": [
                "id": 1,
                "age": 0,
                "cashAmount": 1000.1,
                "cashAvailable": 50.5,
                "cashInBank": 0,
                "stepsDoneOverall": null,
                "stepsDoneToday": 0,
                "name": "Max",
                "bornOn": [
                    "dayOfYear": 177,
                    "dayOfWeek": "SUNDAY",
                    "month": "JUNE",
                    "dayOfMonth": 26,
                    "year": 1994,
                    "monthValue": 6,
                    "hour": 12,
                    "minute": 30,
                    "second": 15,
                    "nano": 0,
                    "chronology": [
                        "calendarType": "iso8601",
                        "id": "ISO"
                    ]
                ],
                "lastOnLineOn": [
                    "year": 2017,
                    "month": "APRIL",
                    "chronology": [
                        "calendarType": "iso8601",
                        "id": "ISO"
                    ],
                    "era": "CE",
                    "dayOfYear": 105,
                    "dayOfWeek": "SATURDAY",
                    "leapYear": false,
                    "dayOfMonth": 15,
                    "monthValue": 4
                ]
            ]
        ]

        when:
        def result = executor.execute(query).data

        then:
        result == expected;
    }
}
