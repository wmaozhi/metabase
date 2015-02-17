(ns metabase.query-processor.native
  "The `native` query processor."
  (:require [metabase.api.common :refer :all]
            [metabase.db :refer [sel]]
            (metabase.models [database :refer [Database]])))

(defn process [query]
  query)

(defn process-and-run [{:keys [native database] :as query}]
  (println "QUERY: " query)
  (let [db (sel :one Database :id database)
        sql (:query native)
        results ((:native-query db) sql)]
    {:status :completed
     :row_count (count results)
     :data {:rows (map vals results)
            :columns (keys (first results))}}))

(def Q {:database 5
        :type :native
        :native {:query "select substring(substring(substring(dataset_query from '\"type\": ?\"\\w+\"') from ': ?\"\\w+\"') from '\\w+') as typ, count(*)
                 from report_card
                 group by 1
                 order by 1"}})
